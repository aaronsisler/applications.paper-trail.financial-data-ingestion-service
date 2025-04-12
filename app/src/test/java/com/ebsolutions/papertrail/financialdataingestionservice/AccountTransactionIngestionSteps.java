package com.ebsolutions.papertrail.financialdataingestionservice;

import com.ebsolutions.papertrail.financialdataingestionservice.tooling.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Assertions;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

public class AccountTransactionIngestionSteps extends BaseTest {
  private String requestContent;
  private MvcResult result;

  @And("an account transaction in the request body has an invalid institution")
  public void anAccountTransactionInTheRequestBodyHasAnInvalidInstitution() {
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() {
  }

  @Then("the correct bad request response is returned from the ingest transactions endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheIngestTransactionsEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    //    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    //    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }
}
