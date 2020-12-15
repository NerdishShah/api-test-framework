#Author: shahulhameeedmy@gmail.com
@getColorDetails
Feature: Color service
  As a frontend user,
  I want to have a service that returns color details
  So that the information can be used on design tokens

  @getColor @positive
  Scenario Outline: Valid color ID - validate by Jayway JSONPath
    Given the test environment is configured
    When I pass path params as
      | id | <id> |
    * I perform GET operation <resource>
    Then I should get response status code as <status>
    * response content type should be in <format>
    * response body should match JSONPath expression <jPathExpression>

    Examples: 
      | status | id | resource            | format | jPathExpression                                   |
      |    200 |  1 | '/api/unknown/{id}' | 'JSON' | 'data.id:1;data.name:cerulean;data.year:2000'     |
      |    200 |  2 | '/api/unknown/{id}' | 'JSON' | 'data.id:2;data.name:fuchsia rose;data.year:2001' |

  @getColor @contentValidation
  Scenario: Valid color ID - validate by rest assured
    Given the test environment is configured
    When I pass path params as
      | id | 1 |
    * I perform GET operation '/api/unknown/{id}'
    Then I should get response status code as 200
    * the response body should be same as
      | id | name     | year |
      |  1 | cerulean | 2000 |

  @getColor @negative
  Scenario: Enter an invalid color ID
    Given the test environment is configured
    When I pass path params as
      | id | 25 |
    * I perform GET operation "/api/unknown/{id}"
    Then I should get response status code as 404
