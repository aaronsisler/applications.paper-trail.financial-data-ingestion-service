package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
   * @param constraintViolationException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class))
          }),
  })
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handle(
      ConstraintViolationException constraintViolationException) {
    Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();

    if (violations.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(
                      Collections.singletonList(
                          "A mandatory field is missing or something went wrong"))
                  .build()
          );
    }

    List<String> messages = new ArrayList<>();

    violations.forEach(violation ->
        messages.add(
            violation.getPropertyPath().toString()
                .concat("::")
                .concat(violation.getMessage()))
    );

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(messages)
                .build()
        );
  }


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
  public ResponseEntity<ErrorResponse> handle(
      MethodArgumentNotValidException methodArgumentNotValidException) {

    if (methodArgumentNotValidException.getFieldError() != null) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(Collections.singletonList(
                      methodArgumentNotValidException.getFieldError().getDefaultMessage()
                  ))
                  .build()
          );
    }

    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .messages(Collections.singletonList(
                    "Invalid input for a field"
                ))
                .build()
        );
  }


  /**
   * @param httpMediaTypeNotSupportedException caught in controller as thrown from service
   * @return custom response with descriptive error messages
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleException(
      HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException) {

    return ResponseEntity.badRequest().body(ErrorResponse.builder()
        .messages(Collections.singletonList("Media type is not supported"))
        .build());
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
