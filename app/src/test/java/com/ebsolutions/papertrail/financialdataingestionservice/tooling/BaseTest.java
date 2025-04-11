package com.ebsolutions.papertrail.financialdataingestionservice.tooling;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@CucumberContextConfiguration
public class BaseTest {
  @Autowired
  protected MockMvc mockMvc;

  @TestConfiguration
  /*
   * Pulling this into BaseTest since there were issues when it was a separate file
   */
  public static class BaseTestConfiguration {
  }
}
