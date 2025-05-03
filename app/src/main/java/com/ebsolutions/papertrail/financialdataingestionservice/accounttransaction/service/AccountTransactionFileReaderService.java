package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.service;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.exception.AccountTransactionFileException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;


public class AccountTransactionFileReaderService<T extends AccountTransactionDto> {

  private final Class<T> clazz;

  public AccountTransactionFileReaderService(Class<T> clazz) {
    this.clazz = clazz;
  }

  public List<T> process(AccountTransactionFileEnvelope accountTransactionFileEnvelope) {
    if (accountTransactionFileEnvelope.getFile() == null) {
      throw new AccountTransactionFileException("File cannot be null");
    }

    if (accountTransactionFileEnvelope.getFile().isEmpty()) {
      throw new AccountTransactionFileException("File cannot be empty");
    }

    try (Reader reader = new InputStreamReader(
        accountTransactionFileEnvelope.getFile().getInputStream())) {
      AccountTransactionCsvService<T> accountCsvService = new AccountTransactionCsvService<>();
      return accountCsvService.processFile(reader, clazz);
    } catch (IOException e) {
      throw new AccountTransactionFileException("File was not able to be parsed");
    }
  }
}