Feature: Account Transaction: Ingestion

  Scenario: Account transaction is valid returns correct response
    Given application is up
    And the account transaction has a valid file
    And the account id in the account transaction is valid
    And the supported institution in the account transaction is valid
    When the ingest account transactions endpoint is invoked
    Then the correct accepted response is returned from the ingest transactions endpoint


  Scenario Outline: Institution is not valid returns correct error
    Given application is up
    And the account transaction has a valid file
    And the account id in the account transaction is valid
    And the supported institution in the account transaction is not valid
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                     |
      | 400        | Invalid argument: supportedInstitution :: NOT_VALID |

  Scenario Outline: Account Id is not valid returns correct error
    Given application is up
    And an account transaction in the request body has an invalid account id
      | <accountId> |
    And the account transaction has a valid file
    And the supported institution in the account transaction is valid
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | statusCode | responseMessage                     |
      | -1        | 400        | Invalid argument: accountId :: -1   |
      | 0         | 400        | Invalid argument: accountId :: 0    |
      |           | 400        | Invalid argument: accountId :: null |

  Scenario Outline: File is null returns correct error
    Given application is up
    And an account transaction in the request body has a null file
    When the ingest account transactions endpoint is invoked with a null file
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage             |
      | 400        | Media type is not supported |

  Scenario Outline: File is empty returns correct error
    Given application is up
    And the account id in the account transaction is valid
    And the supported institution in the account transaction is valid
    And an account transaction in the request body has an empty file
    When the ingest account transactions endpoint is invoked
    Then the correct bad request response is returned from the ingest transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage      |
      | 400        | File cannot be empty |
