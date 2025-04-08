Feature: Modify Agree
  As a user
  I want to modify the status of an agree
  So that I can change the agree status

  Scenario: Modify agree status
    Given There is an agreement with status "accepted"
    And I login as "student" with password "password"
    And I want to check an agreement with the ID "1"
    When I want to modify the agreement with id "1" with the new status "Rejected"
    Then The state has been successfully modified