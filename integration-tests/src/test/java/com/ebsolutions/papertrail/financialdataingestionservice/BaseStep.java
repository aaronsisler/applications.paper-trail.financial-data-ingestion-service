package com.ebsolutions.papertrail.financialdataingestionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

public class BaseStep {
  protected final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  protected String endpoint = "http://sqs.us-east-1.localhost.localstack.cloud:4566";
  protected String queueUrl = endpoint + "/000000000000/ACCOUNT_TRANSACTION_INGESTION_DATAFLOW";

  protected RestTemplate restTemplate = new RestTemplate();
}
