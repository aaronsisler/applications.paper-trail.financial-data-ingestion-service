package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.service.AccountTransactionFileReaderService;

public abstract class AccountTransactionFileReaderFactory<T extends AccountTransactionDto> {

  public AccountTransactionFileReaderService<T> create(SupportedInstitution supportedInstitution) {
    if (supportedInstitution == null || supportedInstitution.getDtoClass() == null) {
      throw new IllegalArgumentException(
          "Invalid or unsupported card type: " + supportedInstitution);
    }
    return new AccountTransactionFileReaderService<>(getDtoClass(supportedInstitution));
  }

  protected abstract Class<T> getDtoClass(SupportedInstitution supportedInstitution);
}
