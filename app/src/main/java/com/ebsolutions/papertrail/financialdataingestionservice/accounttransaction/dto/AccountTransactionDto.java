package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AccountTransactionDto {
  protected Integer rowId;

  public abstract String getAmount();

  public abstract String getDescription();

  public abstract String getTransactionDate();
}
