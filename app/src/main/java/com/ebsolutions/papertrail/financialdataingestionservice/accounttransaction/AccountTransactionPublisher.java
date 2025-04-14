package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Slf4j
@Component
@RequiredArgsConstructor
public class AccountTransactionPublisher {
  private final SqsClient sqsClient;
  private final ObjectMapper objectMapper;
  private final EventQueue eventQueue;

  public void publish(List<AccountTransaction> accountTransactions) {
    try {
      List<String> messages = accountTransactions
          .stream().map(this::process).filter(Objects::isNull).toList();

      List<SendMessageRequest> sendMessageRequests =
          messages.stream().map(message ->
              SendMessageRequest.builder()
                  .queueUrl(eventQueue.getQueueUrl())
                  .messageBody(message)
                  .build()).toList();

      sendMessageRequests.forEach(sqsClient::sendMessage);
    } catch (Exception exception) {
      log.error(exception.getMessage());
      throw exception;
    }
  }

  private String process(AccountTransaction accountTransaction) {
    try {
      return objectMapper.writeValueAsString(accountTransaction);
    } catch (JsonProcessingException e) {
      log.error("Could not parse into JSON for Account Transaction: {}",
          accountTransaction.getId());
      return null;
    }
  }
}