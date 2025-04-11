package com.ebsolutions.papertrail.financialdataingestionservice.config;

import com.ebsolutions.papertrail.financialdataingestionservice.common.EventQueue;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class EventConfig {
  @Value("${infrastructure.endpoint:`Infrastructure endpoint not found in environment`}")
  protected String endpoint;

  @Value("${infrastructure.messaging.queue-url:`Queue name not found in environment`}")
  protected String queueUrl;

  @Bean
  @Profile({"local", "default", "dev"})
  public SqsClient localSqsClientInstantiation() {
    return SqsClient.builder()
        .region(Region.US_EAST_1)
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(staticCredentialsProvider())
        .build();
  }

  @Bean
  @Profile({"local", "default", "dev"})
  public EventQueue eventQueue() {
    return EventQueue.builder()
        .queueUrl(queueUrl)
        .build();
  }

  public StaticCredentialsProvider staticCredentialsProvider() {
    String awsAccessKeyId = "accessKeyId";
    String awsSecretAccessKey = "secretAccessKey";

    return StaticCredentialsProvider.create(
        AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey));
  }
}