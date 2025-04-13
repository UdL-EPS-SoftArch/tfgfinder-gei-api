Feature: Search External Users
  As an application user
  I want to search external users by company name
  So that I can find specific external partners

  Background:
    Given there is a registered external with username "external4" and password "password" and email "external4@example.app", and company name "TechCorp"
    And I am not logged in

  Scenario: Search external by company name
    Given I log in with username "external4" and password "password"
    When I search for externals with company name "TechCorp"
    Then The response code is 200
    And I should see an external with username "external4"
