package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransactionFileEnvelope;
import org.springframework.stereotype.Service;

@Service
public class AccountTransactionService {
  public void process(AccountTransactionFileEnvelope accountTransactionFileEnvelope) {
    if (accountTransactionFileEnvelope.getFile() == null) {
      throw new AccountTransactionFileException("File cannot be null");
    }

    if (accountTransactionFileEnvelope.getFile().isEmpty()) {
      throw new AccountTransactionFileException("File cannot be empty");
    }
  }
}
