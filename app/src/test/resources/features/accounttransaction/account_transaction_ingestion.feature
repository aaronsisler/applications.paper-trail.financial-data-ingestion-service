Feature: Account Transaction: Ingestion

  Scenario Outline: Account transaction is valid returns correct response
    Given application is up
    And the provided supported institution in the account transaction envelope is valid
      | <supportedInstitution> |
    And the account transaction envelope has a valid file with a valid provided account transaction
      | <accountTransaction> |
    And the account id in the account transaction envelope is valid
    And the correct queue is provided
    And the message succeeds to parse into a string for the queue
    When the ingest account transactions endpoint is invoked
    Then the correct accepted response is returned from the ingest transactions endpoint
    And the account transactions are published to the queue

    Examples:
      | supportedInstitution | accountTransaction                                          |
      | AMEX                 | 03/26/2025,RENEWAL MEMBERSHIP FEE,AARON SISLER,-61004,95.00 |
      | MANUAL               | 14.50,Chipotle,2025-09-13                                   |

  Scenario Outline: Account transaction fails to publish to queue
    Given application is up
    And the account transaction envelope has a valid file with a valid account transaction
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is valid
    And the correct queue is provided
    And the message succeeds to parse into a string for the queue
    And the message fails to publish to the queue
    When the ingest account transactions endpoint is invoked
    Then the correct failure response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                          |
      | 500        | Something went wrong publishing to queue |
#
  Scenario Outline: Account transaction fails to be parsed for the queue
    Given application is up
    And the account transaction envelope has a valid file with a valid account transaction
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is valid
    And the correct queue is provided
    And the message fails to parse into a string for the queue
    When the ingest account transactions endpoint is invoked
    Then the correct failure response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |
    And the account transactions are not published to the queue

    Examples:
      | statusCode | responseMessage                          |
      | 500        | Something went when parsing the messages |

  Scenario Outline: Account transaction is invalid returns correct bad request response
    Given application is up
    And the account transaction envelope has a valid file with an invalid account transaction
      | <accountTransaction> |
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is valid
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountTransaction               | statusCode | responseMessage                                            |
      | 123,,2025-09-13                  | 400        | Row 1 :: Description cannot be blank                       |
      | ,Valid_Description,2025-09-13    | 400        | Row 1 :: Amount cannot be blank                            |
      | ABC,Valid_Description,2025-09-13 | 400        | Row 1 :: Amount is not a valid integer                     |
      | 123,Valid_Description,           | 400        | Row 1 :: Transaction date cannot be blank                  |
      | 123,Valid_Description,ABC        | 400        | Row 1 :: Transaction Date is not a valid format YYYY-MM-DD |
      | 123,Valid_Description,2025-13-25 | 400        | Row 1 :: Transaction Date is not a valid format YYYY-MM-DD |
      | 123,Valid_Description,2025-04-32 | 400        | Row 1 :: Transaction Date is not a valid format YYYY-MM-DD |
#
  Scenario Outline: Institution is not valid returns correct error
    Given application is up
    And the account transaction envelope has a valid file with a valid account transaction
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is not valid
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                     |
      | 400        | Invalid argument: supportedInstitution :: NOT_VALID |

  Scenario Outline: Institution does not match with file returns correct error
    Given application is up
    And the account transaction envelope has a valid file with a valid account transaction
    And the account id in the account transaction envelope is valid
    And the supported institution does not match the account transaction format in the file
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                 |
      | 400        | Row 1 :: Amount cannot be blank |

  Scenario Outline: Account Id is not valid returns correct error
    Given application is up
    And the account transaction envelope in the request body has an invalid account id
      | <accountId> |
    And the account transaction envelope has a valid file with a valid account transaction
    And the supported institution in the account transaction envelope is valid
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | statusCode | responseMessage                     |
      | -1        | 400        | Invalid argument: accountId :: -1   |
      | 0         | 400        | Invalid argument: accountId :: 0    |
      |           | 400        | Invalid argument: accountId :: null |
#
  Scenario Outline: File is null returns correct error
    Given application is up
    And the account transaction envelope in the request body has a null file
    When the ingest account transactions endpoint is invoked with a null file
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage             |
      | 400        | Media type is not supported |
#
  Scenario Outline: File is empty returns correct error
    Given application is up
    And the account id in the account transaction envelope is valid
    And the supported institution in the account transaction envelope is valid
    And the account transaction envelope in the request body has an empty file
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage      |
      | 400        | File cannot be empty |
