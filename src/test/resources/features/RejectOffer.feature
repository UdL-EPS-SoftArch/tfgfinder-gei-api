Feature: Reject Offer
  As an admin
  I want to reject an offer (proposal agreement)
  So that the proposal is not approved for that user

  Scenario: Successfully reject an agreement between the "admin" and the proposal titled "title"
    Given A user with username "admin" exists in the system
    And A proposal with title "title" exists in the system
    And A user with username "student" exists in the system
    And An agreement exists between user "student" and proposal with title "title"
    And I am logged in as user "admin"
    When I reject the agreement between user "student" and proposal with title "title"
    Then The agree status is "REJECTED"
    Then The state has been successfully modified
