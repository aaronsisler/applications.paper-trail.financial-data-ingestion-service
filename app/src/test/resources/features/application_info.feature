Feature: Application Health

  Scenario: Application health check is made available through default endpoints
    Given application is up
    When the info endpoint is invoked
    Then the correct info response is returned
