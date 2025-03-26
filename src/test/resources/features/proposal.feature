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

  Scenario: Retrieve a proposal by title
    Given a proposal with title "AI Project"
    And a description "A research project on AI"
    And a timing "6 months"
    And a speciality "Artificial Intelligence"
    And a kind "Research"
    And keywords "AI, ML, Deep Learning"
    When the proposal is saved
    And I search for proposals with title containing "AI"
    Then I should find at least one proposal

  Scenario: Update a proposal description
    Given a proposal with title "AI Project"
    And a description "A research project on AI"
    When the proposal is saved
    And I update the description to "An advanced AI research project"
    Then the proposal should have the updated description

  Scenario: Delete a proposal
    Given a proposal with title "AI Project"
    When the proposal is saved
    And I delete the proposal
    Then the proposal should not exist in the database

  Scenario: Assign a proposal to a student
    Given a proposal with title "AI Project"
    And a student with name "John Doe"
    When I assign the proposal to the student
    Then the proposal should have the student assigned

  Scenario: Assign a director and co-director to a proposal
    Given a proposal with title "AI Project"
    And a professor with name "Dr. Smith"
    And a co-director with name "Dr. Johnson"
    When I assign Dr. Smith as director
    And I assign Dr. Johnson as co-director
    Then the proposal should have both director and co-director assigned

  Scenario: Retrieve proposals by speciality
    Given a proposal with title "AI Project"
    And a speciality "Artificial Intelligence"
    When the proposal is saved
    And I search for proposals with speciality "Artificial Intelligence"
    Then I should find at least one proposal

  Scenario: Retrieve proposals by keywords
    Given a proposal with title "AI Project"
    And keywords "AI, ML, Deep Learning"
    When the proposal is saved
    And I search for proposals containing keyword "ML"
    Then I should find at least one proposal

  Scenario: Prevent creation of invalid proposal
    Given a proposal with an empty title
    When I try to save the proposal
    Then the proposal should not be stored
    And I should receive a validation error

  Scenario: Assign categories to a proposal
    Given a proposal with title "AI Project"
    And a category "Technology"
    When I assign the category to the proposal
    Then the proposal should have the category assigned
