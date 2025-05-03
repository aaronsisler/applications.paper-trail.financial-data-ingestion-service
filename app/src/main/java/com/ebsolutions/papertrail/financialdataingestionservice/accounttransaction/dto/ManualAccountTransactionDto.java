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
public class ManualAccountTransactionDto extends AccountTransactionDto {
  @NotBlank(message = "Amount cannot be blank")
  @CsvBindByPosition(position = 0)
  private String amount;
  @NotBlank(message = "Description cannot be blank")
  @CsvBindByPosition(position = 1)
  private String description;
  @NotBlank(message = "Transaction date cannot be blank")
  @CsvBindByPosition(position = 2)
  private String transactionDate;
}
