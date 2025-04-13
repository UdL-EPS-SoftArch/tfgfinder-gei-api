Feature: Proposal Management

  Background:
    Given There is a registered user with username "user" and password "password" and email "hola@gmail.com"

  Scenario: Create a new proposal
    Given I login with username "user" and password "password"
    Given a proposal with title "AI Project"
    And a description "A research project on AI"
    And a timing "6 months"
    And a speciality "Artificial Intelligence"
    And a kind "Research"
    And keywords "AI, ML, Deep Learning"
    When the proposal is saved
    Then the proposal should be stored in the database
