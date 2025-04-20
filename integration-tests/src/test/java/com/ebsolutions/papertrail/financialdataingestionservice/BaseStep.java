package com.ebsolutions.papertrail.financialdataingestionservice;

import org.springframework.web.client.RestTemplate;

public class BaseStep {
  protected RestTemplate restTemplate = new RestTemplate();
}
