package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class AccountTransactionSubscriber {
  //  private final SqsAsyncClient sqsAsyncClient;
  private final SqsClient sqsClient;

  @Value("${infrastructure.messaging.queue-url:`Queue name not found in environment`}")
  protected String queueUrl;

  @Async
  @Scheduled(fixedRate = 1000, initialDelay = 1000)
  public void consumeMessages() {
    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
        .queueUrl(queueUrl)
        .maxNumberOfMessages(10)
        .waitTimeSeconds(2)
        .build();


    List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

    // Exit the loop if no messages on the queue
    if (messages.isEmpty()) {
      return;
    }

    List<DeleteMessageBatchRequestEntry> deleteMessageBatchRequestEntries = new ArrayList<>();

    for (Message message : messages) {
      System.out.println("Message");
      System.out.println("Thread: " + Thread.currentThread().threadId());
      System.out.println(message.messageId());
      System.out.println(message.body());
      deleteMessageBatchRequestEntries.add(
          DeleteMessageBatchRequestEntry.builder().id(message.messageId()).build());
    }

    DeleteMessageBatchRequest deleteMessageBatchRequest =
        DeleteMessageBatchRequest.builder().queueUrl(queueUrl)
            .entries(deleteMessageBatchRequestEntries)
            .build();

    sqsClient.deleteMessageBatch(deleteMessageBatchRequest);
  }
}
