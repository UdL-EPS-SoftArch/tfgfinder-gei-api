Feature: External Profile Update
  As an external user
  I want to update my company details
  So that my company information is kept current

  Background:
    Given there is a registered external with username "external2" and password "password" and email "external2@example.app", and company name "Old Company"
    And I am not logged in

  Scenario: Update external user's company name
    Given I log in with username "external2" and password "password"
    When I update the external user's company name to "New Company Inc"
    Then The response code is 200
    And the external user's company name is "New Company Inc"
