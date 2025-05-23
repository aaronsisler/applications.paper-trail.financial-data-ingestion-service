package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.exception.AccountTransactionFileException;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.exception.AccountTransactionPublishException;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
  /**
   * This will be called when a body field does not meet constraints
   *
   * @param methodArgumentNotValidException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException methodArgumentNotValidException) {

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(Collections.singletonList(
                    "Invalid argument: "
                        +
                        Objects.requireNonNull(methodArgumentNotValidException.getFieldError())
                            .getField()
                        + " :: "
                        +
                        methodArgumentNotValidException.getFieldError().getRejectedValue()

                ))
                .build()
        );
  }


  /**
   * @param httpMediaTypeNotSupportedException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException) {

    return ResponseEntity.badRequest().body(ErrorResponse.builder()
        .messages(Collections.singletonList("Media type is not supported"))
        .build());
  }

  /**
   * @param accountTransactionFileException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(AccountTransactionFileException.class)
  public ResponseEntity<ErrorResponse> handleAccountTransactionFileException(
      AccountTransactionFileException accountTransactionFileException) {

    if (accountTransactionFileException.getErrorMessageEnvelopes().isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(Collections.singletonList(accountTransactionFileException.getMessage()))
                  .build()
          );
    }

    List<String> messages =
        accountTransactionFileException.getErrorMessageEnvelopes().stream()
            .map(errorMessageEnvelope ->
                "Row " + errorMessageEnvelope.getRowId()
                    + " :: " + errorMessageEnvelope.getErrorMessage())
            .toList();

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(messages)
                .build()
        );
  }


  /**
   * @param accountTransactionPublishException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(AccountTransactionPublishException.class)
  public ResponseEntity<ErrorResponse> handleAccountTransactionPublishException(
      AccountTransactionPublishException accountTransactionPublishException) {

    return ResponseEntity.internalServerError()
        .body(
            ErrorResponse.builder()
                .messages(
                    Collections.singletonList(accountTransactionPublishException.getMessage()))
                .build()
        );
  }

  /**
   * @param exception caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Exception> handleException(
      Exception exception) {
    log.error("You need to see what exception was actually thrown");
    log.error("Error", exception);

    return ResponseEntity.internalServerError().body(exception);
  }
}
