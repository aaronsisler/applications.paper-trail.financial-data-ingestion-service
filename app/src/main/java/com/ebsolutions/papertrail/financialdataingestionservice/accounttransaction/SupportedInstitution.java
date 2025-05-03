package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AmexAccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.ManualAccountTransactionDto;
import lombok.Getter;
import lombok.NonNull;

@Getter
public enum SupportedInstitution {
  AMEX(AmexAccountTransactionDto.class),
  MANUAL(ManualAccountTransactionDto.class),
  // Add more card types and DTOs here
  ;

  private final Class<?> dtoClass;

  SupportedInstitution(@NonNull Class<?> dtoClass) {
    this.dtoClass = dtoClass;
  }
}