package com.ebsolutions.papertrail.financialdataingestionservice;

import com.ebsolutions.papertrail.financialdataingestionservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.model.SupportedInstitution;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AccountTransactionIngestionSteps extends BaseStep {
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
  public void theAccountTransactionsArePublishedToTheQueueWithTheCorrectAccountTransaction() {
    // Read the queue
    // Check the account transaction is correct
    // Check the account id on the account transaction is correct
  }
}
