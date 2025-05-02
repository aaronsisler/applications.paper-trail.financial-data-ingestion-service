package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionFileReaderFactoryRegistry {

  private final Map<SupportedInstitution, AccountTransactionFileReaderFactory
      <? extends AccountTransactionDto>> factories = new HashMap<>();

  @Autowired
  public AccountTransactionFileReaderFactoryRegistry(
      AmexAccountTransactionFileReaderFactory amexFactory,
      ManualAccountTransactionFileReaderFactory manualFactory
  ) {
    factories.put(SupportedInstitution.AMEX, amexFactory);
    factories.put(SupportedInstitution.MANUAL, manualFactory);
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