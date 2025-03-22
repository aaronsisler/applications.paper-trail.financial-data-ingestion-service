package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTransactionService {
  private final AccountTransactionPublisher accountTransactionPublisher;

  public void publishAll(List<AccountTransaction> accountTransactions) {
    accountTransactionPublisher.publish(accountTransactions);
  }
}
