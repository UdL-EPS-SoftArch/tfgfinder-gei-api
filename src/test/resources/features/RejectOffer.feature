Feature: Delete Offer
  As an admin
  I want to reject an offer (proposal agreement)
  So that the proposal is not approved for that user

  Background:
    Given A user with username "student" exists in the system
    And A proposal with title "IA" exists in the system
    And there is a registered admin with username "admin" and password "password" and email "admin@mail.app"

  Scenario: Attempt to delete an offer without authentication
    Given I login as "student" with password "password"
    When I try to delete the offer "IA" without authentication
    Then The response code is 401

  Scenario: Attempt to delete a non-existing offer
    Given I login as "admin" with password "password"
    When I delete the offer with id "9999"
    Then The response code is 404

  Scenario: Delete an existing offer
    Given An agreement exists between user "student" and proposal with title "IA"
    And I login as "admin" with password "password"
    When I delete the offer with title "IA" by the user "student"
    Then The response code is 204


