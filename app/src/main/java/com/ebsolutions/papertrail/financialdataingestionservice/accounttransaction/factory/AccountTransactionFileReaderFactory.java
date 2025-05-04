package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.service.AccountTransactionFileReaderService;
import lombok.NonNull;

public abstract class AccountTransactionFileReaderFactory<T extends AccountTransactionDto> {

  public AccountTransactionFileReaderService<T> create(
      @NonNull SupportedInstitution supportedInstitution) {

    return new AccountTransactionFileReaderService<>(getDtoClass(supportedInstitution));
  }

  protected abstract Class<T> getDtoClass(SupportedInstitution supportedInstitution);
}
