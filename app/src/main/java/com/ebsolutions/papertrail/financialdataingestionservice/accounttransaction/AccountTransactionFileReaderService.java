package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;


public class AccountTransactionFileReaderService<T extends AccountTransactionDto> {

  private final Class<T> clazz;

  public AccountTransactionFileReaderService(Class<T> clazz) {
    this.clazz = clazz;
  }

  public List<T> process(AccountTransactionFileEnvelope accountTransactionFileEnvelope)
      throws Exception {
    if (accountTransactionFileEnvelope.getFile() == null) {
      throw new Exception("File cannot be null");
    }

    if (accountTransactionFileEnvelope.getFile().isEmpty()) {
      throw new Exception("File cannot be empty");
    }

    try (Reader reader = new InputStreamReader(
        accountTransactionFileEnvelope.getFile().getInputStream())) {
      AccountTransactionCsvService<T> accountCsvService = new AccountTransactionCsvService<>();
      return accountCsvService.processFile(reader, clazz);
    } catch (IOException e) {
      throw new Exception("File was not able to be parsed", e);
    }
  }
}