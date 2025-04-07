package com.ebsolutions.papertrail.financialdataingestionservice.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageEnvelope {
  private int rowId;
  private String errorMessage;
}
