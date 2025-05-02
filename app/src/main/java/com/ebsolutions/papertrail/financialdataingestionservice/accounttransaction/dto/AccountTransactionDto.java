package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto;

public abstract class AccountTransactionDto {
  public abstract Integer getRowId();

  public abstract String getAmount();

  public abstract String getDescription();

  public abstract String getTransactionDate();
}
