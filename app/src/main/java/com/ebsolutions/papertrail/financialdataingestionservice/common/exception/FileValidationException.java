package com.ebsolutions.papertrail.financialdataingestionservice.common.exception;

import com.ebsolutions.papertrail.financialdataingestionservice.common.ErrorMessageEnvelope;
import java.util.List;

public class FileValidationException extends RuntimeException {
  public List<ErrorMessageEnvelope> errorMessageEnvelopes;

  public FileValidationException(String message, List<ErrorMessageEnvelope> errorMessageEnvelopes) {
    super(message);
    this.errorMessageEnvelopes = errorMessageEnvelopes;
  }
}
