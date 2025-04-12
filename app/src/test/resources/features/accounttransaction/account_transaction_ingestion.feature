#Feature: Account Transaction: Ingestion
#
#  Scenario Outline: Institution is not valid returns correct error
#    Given application is up
#    And an account transaction in the request body has an invalid institution
#    When the ingest account transactions endpoint is invoked
#    Then the correct bad request response is returned from the ingest transactions endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                            |
#      | 400        | A provided field is relationally incorrect |
#
#  Scenario Outline: Account Id is not valid returns correct error
#    Given application is up
#    And an account transaction in the request body has an invalid account id
#      | <accountId> |
#    When the ingest account transactions endpoint is invoked
#    Then the correct bad request response is returned from the ingest transactions endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | accountId | statusCode | responseMessage                            |
#      | -1        | 400        | A provided field is relationally incorrect |
#      | 0         | 400        | A provided field is relationally incorrect |
#      |           | 400        | A provided field is relationally incorrect |
#
#  Scenario Outline: File is empty returns correct error
#    Given application is up
#    And an account transaction in the request body has an empty file
#    When the ingest account transactions endpoint is invoked
#    Then the correct bad request response is returned from the ingest transactions endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                            |
#      | 400        | A provided field is relationally incorrect |
#
#
#  Scenario Outline: Account Id does not exist returns correct error
#    Given application is up
#    When the ingest account transactions endpoint is invoked
#    Then the correct bad request response is returned from the ingest transactions endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                            |
#      | 400        | A provided field is relationally incorrect |