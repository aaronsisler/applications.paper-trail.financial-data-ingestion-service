package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AccountTransactionDto {
  protected Integer rowId;
  protected String dateFormat;

  public abstract String getAmount();

  public abstract String getDescription();

  public abstract String getTransactionDate();
}
