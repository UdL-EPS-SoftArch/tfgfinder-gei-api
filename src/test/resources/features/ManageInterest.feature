Feature: Manage Interest
  In order to track users' interests in proposals
  As a user
  I want to create, retrieve, update, and delete my interests

  Scenario: Create a new interest
    Given There is an existing user with username "john_doe"
    And There is an existing proposal with title "AI Research"
    When I create a new interest with status "Pending" and timestamp "2025-03-19T10:00:00Z" for user "john_doe" and proposal "AI Research"
    Then The response code is 201
    And The interest has been created with status "Pending"

  Scenario: Create interest with missing status
    Given There is an existing user with username "john_doe"
    And There is an existing proposal with title "AI Research"
    When I create a new interest with status "" and timestamp "2025-03-19T10:00:00Z" for user "john_doe" and proposal "AI Research"
    Then The response code is 400
    And The error message is "Status must not be blank"
    And It has not been created an interest

  Scenario: Retrieve all interests for a user
    Given There is an existing user with username "john_doe"
    And The user has 2 interests with statuses "Approved" and "Rejected"
    When I retrieve all interests for the user "john_doe"
    Then The response code is 200
    And The response contains 2 interests

  Scenario: Retrieve all interests for a proposal
    Given There is an existing proposal with title "AI Research"
    And The proposal has 3 interests with statuses "Pending", "Approved", and "Rejected"
    When I retrieve all interests for the proposal "AI Research"
    Then The response code is 200
    And The response contains 3 interests

  Scenario: Update an interest status
    Given There is an existing interest with id "1" and status "Pending"
    When I update the interest with id "1" to status "Approved"
    Then The response code is 200
    And The interest has been updated with status "Approved"

  Scenario: Delete an interest
    Given There is an existing interest with id "1"
    When I delete the interest with id "1"
    Then The response code is 204
    And The interest no longer exists
