package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AmexAccountTransactionDto;
import org.springframework.stereotype.Component;

@Component
public class AmexAccountTransactionFileReaderFactory
    extends AccountTransactionFileReaderFactory<AmexAccountTransactionDto> {

  @Override
  protected Class<AmexAccountTransactionDto> getDtoClass(
      SupportedInstitution supportedInstitution) {
    if (supportedInstitution != SupportedInstitution.AMEX) {
      throw new IllegalArgumentException("This factory only supports AMEX");
    }
    return AmexAccountTransactionDto.class;
  }
}
