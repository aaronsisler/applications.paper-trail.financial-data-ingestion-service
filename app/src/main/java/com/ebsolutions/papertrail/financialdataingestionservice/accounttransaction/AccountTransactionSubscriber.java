package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import io.awspring.cloud.sqs.annotation.SqsListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountTransactionSubscriber {

  @SqsListener("${infrastructure.messaging.queue-url}")
  public void receiveRecordMessage(String accountTransaction) {
    log.info("Received message: {}", accountTransaction);
    //    userRepository.save(new User(event.id(), event.username(), event.email()));
  }

  @SqsListener("${infrastructure.messaging.queue-url}")
  public void receiveRecordMessages(List<String> accountTransactions) {
    for (String accountTransaction : accountTransactions) {
      log.info("Received message: {}", accountTransaction);
    }
    //    userRepository.save(new User(event.id(), event.username(), event.email()));
  }
}
