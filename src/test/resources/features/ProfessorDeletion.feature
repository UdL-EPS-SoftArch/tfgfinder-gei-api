Feature: Delete Professor Profile
  As a professor
  I want to delete my profile
  So that my account can be removed

  Background:
    Given there is a registered professor with username "professor3" and password "password" and email "professor3@example.app"
    And I am not logged in

  Scenario: Delete professor profile successfully
    Given I log in with username "professor3" and password "password"
    When I delete the professor with username "professor3"
    Then The response code is 204
    And a GET request for professor with username "professor3" returns a 404 error
