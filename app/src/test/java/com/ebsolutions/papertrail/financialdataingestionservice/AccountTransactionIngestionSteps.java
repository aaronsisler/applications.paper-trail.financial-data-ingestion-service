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

  @And("an account transaction in the request body has an invalid institution")
  public void anAccountTransactionInTheRequestBodyHasAnInvalidInstitution() {

  }

  @And("an account transaction in the request body has a null file")
  public void anAccountTransactionInTheRequestBodyHasANullFile() throws JsonProcessingException {
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

  @And("an account transaction in the request body has an empty file")
  public void anAccountTransactionInTheRequestBodyHasAnEmptyFile() throws JsonProcessingException {
    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);
  }

  @And("the account transaction has a valid file")
  public void theAccountTransactionHasAValidFile() {
    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
            "non-empty-file".getBytes());
  }

  @And("an account transaction in the request body has an invalid account id")
  public void anAccountTransactionInTheRequestBodyHasAnInvalidAccountId(DataTable dataTable) {
    accountId = dataTable.column(0).getFirst();
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(MockMvcRequestBuilders.multipart(UriConstants.ACCOUNT_TRANSACTIONS_URI)
            .file(mockMultipartFile)
            .param("accountId", accountId)
            .param("supportedInstitution", SupportedInstitution.AMEX.getValue()))
        .andReturn();
  }

  @When("the ingest account transactions endpoint is invoked with a null file")
  public void theIngestAccountTransactionsEndpointIsInvokedWithANullFile() throws Exception {
    result = mockMvc.perform(post(UriConstants.ACCOUNT_TRANSACTIONS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
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
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }
}
