Feature: Integration: Account Transaction Ingestion

  Scenario: Ingest Account Transactions and Check Account Transaction Queue
    Given application is up
    And the account transaction envelope has a valid file with a valid account transaction
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is valid
    And the correct queue is provided
    And the message succeeds to parse into a string for the queue
    When the ingest account transactions endpoint is invoked
    Then the correct accepted response is returned from the ingest transactions endpoint
    And the account transactions are published to the queue
