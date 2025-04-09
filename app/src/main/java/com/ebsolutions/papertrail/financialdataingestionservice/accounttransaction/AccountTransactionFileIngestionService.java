package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.common.ErrorMessageEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.common.exception.FileValidationException;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountTransactionFileIngestionService {
  private final AccountTransactionDtoValidator accountTransactionDtoValidator;
  private final AccountTransactionPublisher accountTransactionPublisher;

  public void process(
      AccountTransactionFileIngestionEnvelope accountTransactionFileIngestionEnvelope)
      throws IOException {

    // Verify Account id is valid by calling Provider service here


    // Check if the file is empty
    if (accountTransactionFileIngestionEnvelope.getFile().isEmpty()) {
      throw new FileValidationException("File is empty");
    }


    List<AccountTransactionDto> accountTransactionDtos;

    // Parse the CSV and create a DTO for each row
    try (Reader reader = new InputStreamReader(
        accountTransactionFileIngestionEnvelope.getFile().getInputStream())) {
      CsvToBean<AccountTransactionDto> cb = new CsvToBeanBuilder<AccountTransactionDto>(reader)
          .withType(AccountTransactionDto.class)
          .build();

      accountTransactionDtos = cb.parse();
    }

    // Giving each row/DTO a row id to help with triaging data issues in file
    AtomicInteger atomicInteger = new AtomicInteger(0);
    accountTransactionDtos =
        accountTransactionDtos.stream().map(accountTransactionDto ->
            AccountTransactionDto.builder()
                .rowId(atomicInteger.incrementAndGet())
                .amount(accountTransactionDto.getAmount())
                .description(accountTransactionDto.getDescription())
                .transactionDate(accountTransactionDto.getTransactionDate())
                .build()).toList();


    // Validate each field in the Dto
    // If bad, add to the violations list
    // If any records in the violations list, send a bad request
    List<ErrorMessageEnvelope> errorMessageEnvelopes = new ArrayList<>();
    accountTransactionDtos
        .forEach(accountTransactionDto ->
            errorMessageEnvelopes.addAll(
                accountTransactionDtoValidator.validate(accountTransactionDto)));

    if (!errorMessageEnvelopes.isEmpty()) {
      throw new FileValidationException("File has errors", errorMessageEnvelopes);
    }

    // If no records in the violations list, send back a good request of Account Transactions
    List<AccountTransaction> accountTransactions =
        accountTransactionDtos.stream().map(accountTransactionDto ->
            AccountTransaction.builder()
                .accountId(accountTransactionFileIngestionEnvelope.getAccountId())
                .amount(Integer.parseInt(accountTransactionDto.getAmount()))
                .description(accountTransactionDto.getDescription())
                .transactionDate(LocalDate.parse(accountTransactionDto.getTransactionDate()))
                .build()
        ).toList();

    // Publish the account transactions to stream
    accountTransactionPublisher.publish(accountTransactions);
  }
}
