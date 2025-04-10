Feature: Modify Agree
  As a user
  I want to modify the status of an agree
  So that I can change the agree status

  Scenario: Modify agree status
    Given There is an agreement with title "Test" and status "ACCEPTED"
    And I login as "student" with password "password"
    When I want to modify the agreement with title "Test" with the new status "REJECTED"
    Then The state has been successfully modified