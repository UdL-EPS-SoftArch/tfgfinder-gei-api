Feature: Add Category

Background:
  Given there is a registered admin with username "admin" and password "password" and email "admin@mail.app"

  Scenario: Add a valid category as Admin
    Given I login as "admin" with password "password"
    When I add a new category with name "Software Architecture" and description "Topics related to the design and structure of software systems."
    Then The response code is 201

  Scenario: Add a valid category with only name as Admin
    Given I login as "admin" with password "password"
    When I add a new category with name "Web Development" and no description
    Then The response code is 201

  Scenario: Add category with empty name as Admin
    Given I login as "admin" with password "password"
    When I add a new category with name "" and description "This description should not matter."
    Then The response code is 400

  Scenario: Add category with existing name as Admin
    Given I login as "admin" with password "password"
    And a category with name "Databases" and description "Relational and NoSQL databases" already exists
    When I add a new category with name "Databases" and description "Another description for databases"
    Then The response code is 409

  Scenario: Attempt to add a category as a normal user
    Given I login as "user" with password "password"
    When I try to add a new category with name "Data Science" and description "Machine learning and statistics"
    Then The response code is 401
    And The error message is "Unauthorized"
