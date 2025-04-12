Feature: Create Agreement
  As a user
  I want to create an agreement with a proposal
  So that I can formalize my interest in a proposal

  Scenario: Successfully create an agreement between the "student" with a title "title" with an existing proposal
    Given A user with username "student" exists in the system
    And A proposal with title "title" exists in the system
    And I am logged in as user "student"
    When I select the proposal with title "title" to create an agree
    Then A new agree is created between the user "student" and the proposal  with title "title"
    And The agree status is "PENDING_INTENT"