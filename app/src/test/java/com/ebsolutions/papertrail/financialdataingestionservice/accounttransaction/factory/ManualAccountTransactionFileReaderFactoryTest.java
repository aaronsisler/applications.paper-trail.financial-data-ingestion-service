package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import org.junit.jupiter.api.Test;

public class ManualAccountTransactionFileReaderFactoryTest {
  @Test
  void amexFactoryThrowsExceptionOnNullInput() {
    assertThrows(IllegalArgumentException.class, () -> {

      ManualAccountTransactionFileReaderFactory factory =
          new ManualAccountTransactionFileReaderFactory();

      factory.getDtoClass(null);
    });
  }

  @Test
  void amexFactoryThrowsExceptionOnIncorrectInput() {
    assertThrows(IllegalArgumentException.class, () -> {

      ManualAccountTransactionFileReaderFactory factory =
          new ManualAccountTransactionFileReaderFactory();

      factory.getDtoClass(SupportedInstitution.AMEX);
    });
  }
}
