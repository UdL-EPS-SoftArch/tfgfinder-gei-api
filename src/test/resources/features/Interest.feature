Feature: Interest
  In order to manage interests in proposals
  As a user
  I want to create and manage my interests in proposals

  Background:
    Given there is a registered user with username "user" and password "password" and email "user@sample.app"
    And there is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And there is a created proposal with title "Sample Proposal" and description "Test description"

  Scenario: Create Interest
    Given I login as "user" with password "password"
    When I create an interest for the proposal with title "Sample Proposal"
    Then The response code is 201
    And It has been created an interest with status "PENDING"

  Scenario: Create Interest for non-existing proposal
    Given I login as "user" with password "password"
    When I create an interest for the proposal with title "Non Existing Proposal"
    Then The response code is 404

  Scenario: List Interests
    Given I login as "user" with password "password"
    And I create an interest for the proposal with title "Sample Proposal"
    When I list all interests
    Then The response code is 200

  Scenario: Get Interest by id
    Given I login as "user" with password "password"
    And I create an interest for the proposal with title "Sample Proposal"
    When I get the created interest
    Then The response code is 200
    And The interest status is "PENDING"

  Scenario: Delete Interest
    Given I login as "user" with password "password"
    And I create an interest for the proposal with title "Sample Proposal"
    When I delete the created interest
    Then The response code is 204

  Scenario: Update Interest status
    Given I login as "user" with password "password"
    And I create an interest for the proposal with title "Sample Proposal"
    When I update the interest status to "ACCEPTED"
    Then The response code is 200
    And The interest status is "ACCEPTED" 