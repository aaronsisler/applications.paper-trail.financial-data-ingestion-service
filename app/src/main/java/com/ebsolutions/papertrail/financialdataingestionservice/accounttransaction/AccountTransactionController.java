package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ErrorResponse;
import java.util.Collections;
import java.util.List;
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
  private final AccountTransactionFileIngestionService accountTransactionFileIngestionService;

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<?> loadFile(
      @ModelAttribute
      AccountTransactionFileIngestionEnvelope accountTransactionFileIngestionEnvelope)
      throws Exception {
    if (accountTransactionFileIngestionEnvelope.getFile().isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              ErrorResponse.builder()
                  .messages(Collections.singletonList("File is empty"))
                  .build()
          );
    }

    List<AccountTransaction> accountTransactions =
        accountTransactionFileIngestionService.process(accountTransactionFileIngestionEnvelope);

    return ResponseEntity.ok().body(accountTransactions);
  }
}
