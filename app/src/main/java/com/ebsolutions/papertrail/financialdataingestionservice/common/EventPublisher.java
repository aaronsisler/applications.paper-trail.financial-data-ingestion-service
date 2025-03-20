package com.ebsolutions.papertrail.financialdataingestionservice.common;

import com.ebsolutions.papertrail.financialdataingestionservice.model.AccountTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Component
public class EventPublisher {
  public static final String QUEUE_URL =
      "http://localhost:4566/000000000000/INGESTION_SERVICE_OUTBOUND";
  //      "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/INGESTION_SERVICE_OUTBOUND";

  public void publish(List<AccountTransaction> accountTransactions) {

    try {
      SqsClient sqsClient = SqsClient.builder()
          .region(Region.US_EAST_1)
          //          .credentialsProvider(ProfileCredentialsProvider.create())
          .build();

      Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
      MessageAttributeValue messageAttributeValue = MessageAttributeValue.builder()
          .stringValue("This is an attribute")
          .dataType("String")
          .build();

      messageAttributes.put("AttributeOne", messageAttributeValue);

      SendMessageRequest sendMessageStandardQueue = SendMessageRequest.builder()
          .queueUrl(QUEUE_URL)
          .messageBody("A simple message.")
          .messageAttributes(messageAttributes)
          .build();

      sqsClient.sendMessage(sendMessageStandardQueue);
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }
}
