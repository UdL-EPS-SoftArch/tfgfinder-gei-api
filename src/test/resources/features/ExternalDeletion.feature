Feature: Delete External Profile
  As an external user
  I want to delete my profile
  So that my account is removed from the system

  Background:
    Given there is a registered external with username "external3" and password "password" and email "external3@example.app", and company name "Old Company"
    And I am not logged in

  Scenario: Delete external profile successfully
    Given I log in with username "external3" and password "password"
    When I delete the external with username "external3"
    Then The response code is 204
    And a GET request for external with username "external3" returns a 404 error
