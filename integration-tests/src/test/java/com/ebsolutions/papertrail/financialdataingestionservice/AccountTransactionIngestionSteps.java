package com.ebsolutions.papertrail.financialdataingestionservice;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountTransactionIngestionSteps {
  @And("the account transaction envelope has a valid file with a valid account transaction")
  public void theAccountTransactionEnvelopeHasAValidFileWithAValidAccountTransaction() {
  }

  @And("the account id in the account transaction envelope is valid")
  public void theAccountIdInTheAccountTransactionEnvelopeIsValid() {
  }

  @And("the supported institution in the account transaction envelope is valid")
  public void theSupportedInstitutionInTheAccountTransactionEnvelopeIsValid() {
  }

  @And("the correct queue is provided")
  public void theCorrectQueueIsProvided() {
  }

  @And("the message succeeds to parse into a string for the queue")
  public void theMessageSucceedsToParseIntoAStringForTheQueue() {
  }

  @When("the ingest account transactions endpoint is invoked")
  public void theIngestAccountTransactionsEndpointIsInvoked() {
  }

  @Then("the correct accepted response is returned from the ingest transactions endpoint")
  public void theCorrectAcceptedResponseIsReturnedFromTheIngestTransactionsEndpoint() {
  }

  @And("the account transactions are published to the queue")
  public void theAccountTransactionsArePublishedToTheQueue() {
  }
}
