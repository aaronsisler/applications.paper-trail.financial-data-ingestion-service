package com.ebsolutions.papertrail.financialdataingestionservice;

import com.ebsolutions.papertrail.financialdataingestionservice.config.TestConstants;
import com.ebsolutions.papertrail.financialdataingestionservice.model.HealthCheck;
import io.cucumber.java.en.Given;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;

public class ActuatorSteps extends BaseStep {
  private boolean isApplicationUp = false;

  private boolean checkIfApplicationIsUp() {
    try {
      HealthCheck healthCheck = restClient
          .get()
          .uri(TestConstants.HEALTH_CHECK_URI)
          .retrieve()
          .body(HealthCheck.class);

      if (healthCheck == null) {
        return false;
      }

      return "UP".equals(healthCheck.getStatus());
    } catch (Exception exception) {
      return false;
    }
  }

  @Given("application is up")
  public void applicationIsUp() {
    Instant pollingEnd =
        Instant.now().plusMillis(TestConstants.APPLICATION_START_TIME_WAIT_PERIOD_IN_MILLISECONDS);
    while (!isApplicationUp) {
      try {
        Thread.sleep(100);
        if (checkIfApplicationIsUp()) {
          isApplicationUp = true;
          break;
        }
        if (Instant.now().isAfter(pollingEnd)) {
          break;
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted while waiting for condition", e);
      }
    }
    Assertions.assertTrue(isApplicationUp, "Application did not come up in time");
  }
}
