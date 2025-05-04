package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.SupportedInstitution;
import org.junit.jupiter.api.Test;

public class AmexAccountTransactionFileReaderFactoryTest {
  @Test
  void amexFactoryThrowsExceptionOnNullInput() {
    assertThrows(IllegalArgumentException.class, () -> {
      AmexAccountTransactionFileReaderFactory factory =
          new AmexAccountTransactionFileReaderFactory();
      factory.getDtoClass(null);
    });
  }

  @Test
  void amexFactoryThrowsExceptionOnIncorretInput() {
    assertThrows(IllegalArgumentException.class, () -> {
      AmexAccountTransactionFileReaderFactory factory =
          new AmexAccountTransactionFileReaderFactory();
      factory.getDtoClass(SupportedInstitution.MANUAL);
    });
  }
}
