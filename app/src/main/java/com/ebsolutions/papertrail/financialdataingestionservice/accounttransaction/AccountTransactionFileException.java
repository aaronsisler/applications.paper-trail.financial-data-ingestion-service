package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionFileException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1;

  private final List<ErrorMessageEnvelope> errorMessageEnvelopes = new ArrayList<>();

  public AccountTransactionFileException(String message,
                                         List<ErrorMessageEnvelope> errorMessageEnvelopes) {
    super(message);
    this.errorMessageEnvelopes.addAll(errorMessageEnvelopes);
  }

  public AccountTransactionFileException(String message) {
    super(message);
  }
}
