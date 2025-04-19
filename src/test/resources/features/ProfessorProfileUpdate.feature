Feature: Professor Profile Update
  As a professor
  I want to update my profile information
  So that my details remain up to date

  Background:
    Given there is a registered professor with username "professor2" and password "password" and email "professor2@example.app"
    And I am not logged in

  Scenario: Update professor's office information
    Given I log in with username "professor2" and password "password"
    When I update the professor's office to "Room 202"
    Then The response code is 200
    And the professor's office is "Room 202"
