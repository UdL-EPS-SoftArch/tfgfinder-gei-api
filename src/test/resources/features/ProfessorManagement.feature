Feature: Professor Management
  As a professor
  I want to register and manage my profile
  So that I can oversee academic responsibilities

  Background:
    Given there is no registered professor with username "professor1"
    And I am not logged in

  Scenario: Register and login as professor
    When I register a new professor with username "professor1", email "professor1@example.app", password "password", department "CS", center "Engineering", and office "Room 101"
    Then The response code is 201
    And a professor with username "professor1" and email "professor1@example.app" has been created, and the password is not returned
    And I can log in with username "professor1" and password "password"
    And the professor's department is "CS"
