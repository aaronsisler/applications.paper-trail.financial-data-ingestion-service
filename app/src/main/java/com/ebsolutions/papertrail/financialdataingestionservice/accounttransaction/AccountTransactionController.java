package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory.AccountTransactionFileReaderFactory;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory.AccountTransactionFileReaderFactoryRegistry;
import jakarta.validation.Valid;
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

  private final AccountTransactionFileReaderFactoryRegistry factoryRegistry;

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<?> loadFile(
      @ModelAttribute @Valid AccountTransactionFileEnvelope accountTransactionFileEnvelope)
      throws Exception {

    AccountTransactionFileReaderFactory<? extends AccountTransactionDto> factory =
        factoryRegistry.getFactory(accountTransactionFileEnvelope.getSupportedInstitution());

    AccountTransactionFileReaderService<? extends AccountTransactionDto> readerService =
        factory.create(accountTransactionFileEnvelope.getSupportedInstitution());
    List<? extends AccountTransactionDto> result =
        readerService.process(accountTransactionFileEnvelope);
    return ResponseEntity.ok(result);
  }
}