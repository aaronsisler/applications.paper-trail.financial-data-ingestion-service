package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import java.util.Collections;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("account-transactions")
@Slf4j
public class AccountTransactionController {

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<?> loadFile(
      @ModelAttribute AccountTransactionFileEnvelope accountTransactionFileEnvelope) {
    if (accountTransactionFileEnvelope.getFile() == null) {
      return ResponseEntity
          .badRequest()
          .body(ErrorResponse.builder()
              .messages(Collections.singletonList("File cannot be null"))
              .build());
    }

    if (accountTransactionFileEnvelope.getFile().isEmpty()) {
      return ResponseEntity
          .badRequest()
          .body(ErrorResponse.builder()
              .messages(Collections.singletonList("File cannot be empty"))
              .build());
    }


    return ResponseEntity
        .accepted()
        .build();
  }
}