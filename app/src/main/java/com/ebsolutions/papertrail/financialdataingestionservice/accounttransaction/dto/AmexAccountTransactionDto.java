package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AmexAccountTransactionDto extends AccountTransactionDto {
  protected Integer rowId;
  @NotBlank(message = "Amount cannot be blank")
  @CsvBindByPosition(position = 4)
  protected String amount;
  @NotBlank(message = "Description cannot be blank")
  @CsvBindByPosition(position = 1)
  protected String description;
  @NotBlank(message = "Transaction date cannot be blank")
  @CsvBindByPosition(position = 0)
  protected String transactionDate;

  @Override
  public void setRowId(Integer rowId) {
    this.rowId = rowId;
  }
}
