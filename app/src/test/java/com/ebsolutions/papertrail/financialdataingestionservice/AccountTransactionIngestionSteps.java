package com.ebsolutions.papertrail.financialdataingestionservice;

import com.ebsolutions.papertrail.financialdataingestionservice.config.UriConstants;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataingestionservice.model.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
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

  @And("an account transaction in the request body has an invalid institution")
  public void anAccountTransactionInTheRequestBodyHasAnInvalidInstitution() {

  }

  @And("an account transaction in the request body has a null file")
  public void anAccountTransactionInTheRequestBodyHasANullFile() throws JsonProcessingException {
    mockMultipartFile = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
        (byte[]) null);
  }

  @And("an account transaction in the request body has an empty file")
  public void anAccountTransactionInTheRequestBodyHasAnEmptyFile() throws JsonProcessingException {
    mockMultipartFile =
        new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, new byte[0]);
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(MockMvcRequestBuilders.multipart(UriConstants.ACCOUNT_TRANSACTIONS_URI)
            .file(mockMultipartFile)
            .param("accountId", String.valueOf(1))
            .param("supportedInstitution", SupportedInstitution.AMEX.getValue()))
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
