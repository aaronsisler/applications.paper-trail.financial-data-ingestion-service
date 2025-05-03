package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.service;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.AccountTransactionFileEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.AccountTransactionPublisher;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.AccountTransactionValidator;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.ErrorMessageEnvelope;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.exception.AccountTransactionFileException;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory.AccountTransactionFileReaderFactory;
import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.factory.AccountTransactionFileReaderFactoryRegistry;
import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
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
public class AccountTransactionIngestionOrchestrationService {
  private final AccountTransactionFileReaderFactoryRegistry factoryRegistry;
  private final AccountTransactionValidator accountTransactionValidator;
  private final AccountTransactionPublisher accountTransactionPublisher;

  public void process(AccountTransactionFileEnvelope accountTransactionFileEnvelope)
      throws Exception {
    AccountTransactionFileReaderFactory<? extends AccountTransactionDto>
        accountTransactionFileReaderFactory =
        factoryRegistry.getFactory(accountTransactionFileEnvelope.getSupportedInstitution());

    AccountTransactionFileReaderService<? extends AccountTransactionDto>
        accountTransactionFileReaderService =
        accountTransactionFileReaderFactory.create(
            accountTransactionFileEnvelope.getSupportedInstitution());

    List<? extends AccountTransactionDto> accountTransactionDtos =
        accountTransactionFileReaderService.process(accountTransactionFileEnvelope);

    // Giving each row/DTO a row id to help with triaging data issues in file
    AtomicInteger atomicInteger = new AtomicInteger(0);
    for (AccountTransactionDto accountTransactionDto : accountTransactionDtos) {
      accountTransactionDto.setRowId(atomicInteger.incrementAndGet());
    }

    for (AccountTransactionDto accountTransactionDto : accountTransactionDtos) {
      System.out.println(accountTransactionDto.getRowId());
    }

    // Validate each field in the Dto
    // If bad, add to the violations list
    // If any records in the violations list, send a bad request
    List<ErrorMessageEnvelope> errorMessageEnvelopes = new ArrayList<>();
    accountTransactionDtos
        .forEach(accountTransactionDto ->
            errorMessageEnvelopes.addAll(
                accountTransactionValidator.validate(accountTransactionDto)));

    if (!errorMessageEnvelopes.isEmpty()) {
      throw new AccountTransactionFileException("File has errors", errorMessageEnvelopes);
    }

    // If no records in the violations list, send back a good request of Account Transactions
    List<AccountTransaction> accountTransactions =
        accountTransactionDtos.stream().map(accountTransactionDto ->
            AccountTransaction.builder()
                .accountId(accountTransactionFileEnvelope.getAccountId())
                .amount(Integer.parseInt(accountTransactionDto.getAmount()))
                .description(accountTransactionDto.getDescription())
                .transactionDate(LocalDate.parse(accountTransactionDto.getTransactionDate()))
                .build()
        ).toList();

    // Publish the account transactions to stream
    //    accountTransactionPublisher.publish(accountTransactions);
  }
}
