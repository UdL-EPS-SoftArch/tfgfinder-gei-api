Feature: View Available Proposals
  As a student
  I want to view all available proposals
  So that I can decide which one to apply for

  Background:
    Given there is a registered student with username "student4" and password "password" and email "student4@example.app"
    And I am not logged in
    And there exist proposals with titles "Proposal 1" and "Proposal 2"

  Scenario: List available proposals
    Given I log in with username "student4" and password "password"
    When I list all proposals
    Then The response code is 200
    And I should see a proposal with title "Proposal 1"
