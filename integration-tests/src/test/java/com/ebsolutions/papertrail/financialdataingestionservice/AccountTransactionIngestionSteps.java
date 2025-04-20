package com.ebsolutions.papertrail.financialdataingestionservice;

import com.ebsolutions.papertrail.financialdataingestionservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.model.SupportedInstitution;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import software.amazon.awssdk.services.sqs.model.Message;

public class AccountTransactionIngestionSteps extends BaseStep {
  @Autowired
  protected QueueMessageUtil queueMessageUtil = new QueueMessageUtil(endpoint, queueUrl);
  private ResponseEntity<AccountTransactionFileEnvelope> response;
  private MockMultipartFile mockMultipartFile;
  private String accountId;
  private String supportedInstitution;

  @And("the account transaction envelope has a valid file with a valid account transaction")
  public void theAccountTransactionEnvelopeHasAValidFileWithAValidAccountTransaction() {
    mockMultipartFile =
        new MockMultipartFile("file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "1450,Chipotle,2025-09-13".getBytes()
        );
  }

  @And("the account id in the account transaction envelope is valid")
  public void theAccountIdInTheAccountTransactionEnvelopeIsValid() {
    accountId = "1";
  }

  @And("the supported institution in the account transaction envelope is valid")
  public void theSupportedInstitutionInTheAccountTransactionEnvelopeIsValid() {
    supportedInstitution = SupportedInstitution.AMEX.getValue();
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    ByteArrayResource fileAsResource = new ByteArrayResource(mockMultipartFile.getBytes()) {
      @Override
      public String getFilename() {
        return mockMultipartFile.getOriginalFilename();
      }
    };

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("accountId", accountId);
    body.add("supportedInstitution", supportedInstitution);
    body.add("file", fileAsResource);

    HttpEntity<MultiValueMap<String, Object>> requestEntity
        = new HttpEntity<>(body, headers);

    response =
        restTemplate.postForEntity(
            TestConstants.BASE_URL + "/" + TestConstants.ACCOUNT_TRANSACTIONS_URI,
            requestEntity,
            AccountTransactionFileEnvelope.class);
  }

  @Then("the correct accepted response is returned from the ingest transactions endpoint")
  public void theCorrectAcceptedResponseIsReturnedFromTheIngestTransactionsEndpoint() {
    Assertions.assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
  }

  @And("the account transactions are published to the queue with the correct account transaction")
  public void theAccountTransactionsArePublishedToTheQueueWithTheCorrectAccountTransaction()
      throws JsonProcessingException, InterruptedException {
    List<Message> messages;
    Instant pollingEnd =
        Instant.now().plusMillis(TestConstants.QUEUE_POLLING_WAIT_PERIOD_IN_MILLISECONDS);
    System.out.println("QUEuE");
    System.out.println(queueUrl);

    do {
      // Wait between each polling since consumer is fast
      Thread.sleep(100);
      // Check if any messages
      messages = queueMessageUtil.consume();
      // If messages are found, break for the assertions
      if (!messages.isEmpty()) {
        break;
      }
    } while (!Instant.now().isAfter(pollingEnd));

    // If not messages found after timeout, fail test
    if (messages.isEmpty()) {
      Assertions.fail("We didn't find any messages on the queue");
    }

    // Check the account transaction is correct
    AccountTransaction accountTransaction =
        objectMapper.readValue(messages.getFirst().body(), AccountTransaction.class);

    // Check the account id on the account transaction is correct
    Assertions.assertEquals(1450, accountTransaction.getAmount());
    Assertions.assertEquals("Chipotle", accountTransaction.getDescription());
    Assertions.assertEquals(LocalDate.of(2025, 9, 13), accountTransaction.getTransactionDate());
    Assertions.assertEquals(Integer.parseInt(accountId), accountTransaction.getAccountId());
  }
}
