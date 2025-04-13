package com.ebsolutions.papertrail.financialdataingestionservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataingestionservice.config.UriConstants;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataingestionservice.model.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AccountTransactionIngestionSteps extends BaseTest {
  private String requestContent;
  private MvcResult result;
  private MockMultipartFile mockMultipartFile;
  private String accountId;
  private String supportedInstitution;

  @And("the account transaction envelope has a valid file with a valid account transaction")
  public void theAccountTransactionEnvelopeHasAValidFileWithAValidAccountTransaction() {
    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
            "1450,Chipotle,2025-09-13".getBytes());
  }

  @And("the account transaction envelope has a valid file with an invalid account transaction")
  public void theAccountTransactionEnvelopeHasAValidFileWithAnInvalidAccountTransaction(
      DataTable dataTable) {
    String fileContent = dataTable.column(0).getFirst();

    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
            fileContent.getBytes());
  }

  @And("the account transaction envelope in the request body has an invalid institution")
  public void theAccountTransactionEnvelopeInTheRequestBodyHasAnInvalidInstitution()
      throws JsonProcessingException {
    AccountTransactionFileEnvelope accountTransactionFileEnvelope =
        AccountTransactionFileEnvelope
            .builder()
            .accountId(1)
            .file(null)
            .supportedInstitution(SupportedInstitution.AMEX)
            .build();

    requestContent =
        objectMapper.writeValueAsString(
            Collections.singletonList(accountTransactionFileEnvelope));
  }

  @And("the account transaction envelope in the request body has a null file")
  public void theAccountTransactionEnvelopeInTheRequestBodyHasANullFile()
      throws JsonProcessingException {
    AccountTransactionFileEnvelope accountTransactionFileEnvelope =
        AccountTransactionFileEnvelope
            .builder()
            .accountId(1)
            .file(null)
            .supportedInstitution(SupportedInstitution.AMEX)
            .build();

    requestContent =
        objectMapper.writeValueAsString(
            Collections.singletonList(accountTransactionFileEnvelope));
  }

  @And("the account transaction envelope in the request body has an empty file")
  public void theAccountTransactionEnvelopeInTheRequestBodyHasAnEmptyFile() {
    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);
  }


  @And("the account id in the account transaction envelope is valid")
  public void theAccountIdInTheAccountTransactionEnvelopeIsValid() {
    accountId = "1";
  }

  @And("the account transaction envelope in the request body has an invalid account id")
  public void theAccountTransactionEnvelopeInTheRequestBodyHasAnInvalidAccountId(
      DataTable dataTable) {
    accountId = dataTable.column(0).getFirst();
  }

  @And("the supported institution in the account transaction envelope is valid")
  public void theSupportedInstitutionInTheAccountTransactionEnvelopeIsValid() {
    supportedInstitution = SupportedInstitution.AMEX.getValue();
  }

  @And("the supported institution in the account transaction envelope is not valid")
  public void theSupportedInstitutionInTheAccountTransactionEnvelopeIsNotValid() {
    supportedInstitution = "NOT_VALID";
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(MockMvcRequestBuilders.multipart(UriConstants.ACCOUNT_TRANSACTIONS_URI)
            .file(mockMultipartFile)
            .param("accountId", accountId)
            .param("supportedInstitution", supportedInstitution))
        .andReturn();
  }

  @Then("the correct bad request response is returned from the ingest transactions endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheIngestTransactionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    if (String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
        .equals(dataTable.column(0).getFirst())) {
      return;
    }

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);

    List<String> matchingErrorMessages = errorResponse.getMessages().stream()
        .filter(message -> dataTable.column(1).getFirst().equals(message)).toList();

    if (matchingErrorMessages.isEmpty()) {
      System.out.println(errorResponse.getMessages());
      Assertions.fail("Error message not found: ".concat(dataTable.column(1).getFirst()));
    }
  }

  @When("the ingest account transactions endpoint is invoked with a null file")
  public void theIngestAccountTransactionsEndpointIsInvokedWithANullFile() throws Exception {
    result = mockMvc.perform(post(UriConstants.ACCOUNT_TRANSACTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the correct accepted response is returned from the ingest transactions endpoint")
  public void theCorrectAcceptedResponseIsReturnedFromTheIngestTransactionsEndpoint() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.ACCEPTED.value(), mockHttpServletResponse.getStatus());
  }
}
