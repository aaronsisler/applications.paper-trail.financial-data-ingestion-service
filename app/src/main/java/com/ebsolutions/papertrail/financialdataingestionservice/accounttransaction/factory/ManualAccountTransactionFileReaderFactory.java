package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.ManualAccountTransactionDto;
import org.springframework.stereotype.Component;

@Component
public class ManualAccountTransactionFileReaderFactory
    extends AccountTransactionFileReaderFactory<ManualAccountTransactionDto> {

  @Override
  protected Class<ManualAccountTransactionDto> getDtoClass(
      SupportedInstitution supportedInstitution) {
    if (supportedInstitution != SupportedInstitution.MANUAL) {
      throw new IllegalArgumentException("This factory only supports AMEX");
    }
    return ManualAccountTransactionDto.class;
  }
}
