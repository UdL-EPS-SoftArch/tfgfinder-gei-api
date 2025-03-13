Feature: Proposal Management

  Scenario: Create a new proposal
    Given a proposal with title "AI Project"
    And a description "A research project on AI"
    And a timing "6 months"
    And a speciality "Artificial Intelligence"
    And a kind "Research"
    And keywords "AI, ML, Deep Learning"
    When the proposal is saved
    Then the proposal should be stored in the database
