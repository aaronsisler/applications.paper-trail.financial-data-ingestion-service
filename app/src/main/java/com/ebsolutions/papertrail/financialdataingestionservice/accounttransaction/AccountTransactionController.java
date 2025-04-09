package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.common.ReceiptUtil;
import com.ebsolutions.papertrail.financialdataingestionservice.model.ReceiptResponse;
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

    accountTransactionFileIngestionService.process(accountTransactionFileIngestionEnvelope);

    return ResponseEntity
        .accepted()
        .body(
            ReceiptResponse.builder()
                .receiptId(ReceiptUtil.createReceipt())
                .build()
        );
  }
}
