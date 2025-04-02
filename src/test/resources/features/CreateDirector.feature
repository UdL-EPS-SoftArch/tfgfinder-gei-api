Feature: Create Director
    In order to look for properties as a Director
    I want to create a Director profile

Scenario: Create a director with valid data
  Given I am not logget in
  When I create a director with username "director" and password "password" and email "director@director.app" and phoneNumber "123123123" and name "Director"
  Then The response code is 201
  And There is 1 Director created with username "director" and email "director@director.app" and phoneNumber "123123123" and name "Director"

  Scenario: Create a director with a blank email:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "" and phoneNumber "123123123" and name "Director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with a blank password:
    Given I am not logged in
    When I create a director with username "director" and password "" and email "director@director.app" and phoneNumber "123123123" and name "director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with an empty phoneNumber:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "director@director.app" and phoneNumber "" and name "director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with an empty name:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "director@director.app" and phoneNumber "123123123" and name ""
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with an empty username:
    Given I am not logged in
    When I create a director with username "" and password "password" and email "director@director.app" and phoneNumber "123123123" and name ""
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with a null email:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "null" and phoneNumber "123123123" and name "director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with a null password:
    Given I am not logged in
    When I create a director with username "director" and password "null" and email "director@director.app" and phoneNumber "123123123" and name "director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with an null phoneNumber:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "director@director.app" and phoneNumber "null" and name "director"
    Then The response code is 400
    And There is 0 Director created

  Scenario: Create a director with an null name:
    Given I am not logged in
    When I create a director with username "director" and password "password" and email "director@director.app" and phoneNumber "123123123" and name "null"
    Then The response code is 400
    And There is 0 Director created


  Scenario: Create a director with a null username:
    Given I am not logged in
    When I create a director with username "null" and password "password" and email "director@director.app" and phoneNumber "123123123" and name ""
    Then The response code is 400
    And There is 0 Director create