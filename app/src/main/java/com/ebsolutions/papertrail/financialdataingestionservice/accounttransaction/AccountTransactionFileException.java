package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import java.util.ArrayList;
import java.util.List;

public class AccountTransactionFileException extends RuntimeException {
  public List<ErrorMessageEnvelope> errorMessageEnvelopes = new ArrayList<>();

  public AccountTransactionFileException(String message,
                                         List<ErrorMessageEnvelope> errorMessageEnvelopes) {
    super(message);
    this.errorMessageEnvelopes = errorMessageEnvelopes;
  }

  public AccountTransactionFileException(String message) {
    super(message);
  }
}
