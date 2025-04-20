package com.ebsolutions.papertrail.financialdataingestionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.web.client.RestTemplate;

public class BaseStep {
  protected final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
  protected RestTemplate restTemplate = new RestTemplate();

  @TestConfiguration
  public static class BaseTestConfiguration {

  }
}
