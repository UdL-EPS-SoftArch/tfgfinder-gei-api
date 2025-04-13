Feature: External User Management
  As an external user
  I want to register and manage my profile details
  So that I can participate in external collaborations

  Background:
    Given there is no registered external with username "external1"
    And I am not logged in

  Scenario: Register and log in as external user
    When I register a new external with username "external1", email "external1@example.app", password "password", and company name "Acme Corp"
    Then The response code is 201
    And an external with username "external1" and email "external1@example.app" has been created, and the password is not returned
    And I can log in with username "external1" and password "password"
    And the external user's company name is "Acme Corp"
