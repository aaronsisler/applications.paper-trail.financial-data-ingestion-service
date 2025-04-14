package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventQueue {
  private String queueUrl;
}
