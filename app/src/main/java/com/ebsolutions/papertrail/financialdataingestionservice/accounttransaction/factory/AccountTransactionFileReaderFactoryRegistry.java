package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionFileReaderFactoryRegistry {

  private final Map<SupportedInstitution, AccountTransactionFileReaderFactory
      <? extends AccountTransactionDto>> factories;

  public AccountTransactionFileReaderFactoryRegistry(
      AmexAccountTransactionFileReaderFactory amexFactory,
      ManualAccountTransactionFileReaderFactory manualFactory
  ) {
    factories = Map.of(
        SupportedInstitution.AMEX, amexFactory,
        SupportedInstitution.MANUAL, manualFactory
    );
  }

  public AccountTransactionFileReaderFactory<? extends AccountTransactionDto> getFactory(
      SupportedInstitution institution) {
    AccountTransactionFileReaderFactory<? extends AccountTransactionDto> factory =
        factories.get(institution);
    if (factory == null) {
      throw new IllegalArgumentException("Unsupported institution: " + institution);
    }
    return factory;
  }
}