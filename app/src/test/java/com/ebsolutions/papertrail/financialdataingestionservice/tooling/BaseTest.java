package com.ebsolutions.papertrail.financialdataingestionservice.tooling;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.EventQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class BaseTest {
  protected final ObjectMapper objectMapper = new ObjectMapper()
      .findAndRegisterModules()
      // Needed to allow for empty file to be sent
      .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

  @Autowired
  protected MockMvc mockMvc;

  @TestConfiguration
  /*
   * Pulling this into BaseTest since there were issues when it was a separate file
   */
  public static class BaseTestConfiguration {
    @MockBean
    protected SqsClient sqsClient;

    @MockBean
    protected EventQueue eventQueue;
  }
}
