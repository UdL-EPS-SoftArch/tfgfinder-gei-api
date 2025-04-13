Feature: Student Management
  In order to manage access to the application as a student
  As a student user
  I want to be able to register, log in, and retrieve my information

  Scenario: Register and log in as student
    Given there is no registered student with username "student1"
    And I am not logged in
    When I register a new student with username "student1", email "student1@example.app" and password "password"
    Then The response code is 201
    And a student with username "student1" and email "student1@example.app" has been created, and the password is not returned
    And I can log in with username "student1" and password "password"

  Scenario: Log in with a non-existing student
    Given there isn't a registered student with username "nonexistent"
    And I am not logged in
    When I log in with username "nonexistent" and password "password"
    Then The response code is 401

  Scenario: Retrieve student details
    Given there is a registered student with username "student2" and password "password" and email "student2@example.app"
    And I log in with username "student2" and password "password"
    When I get the student details for username "student2"
    Then The response code is 200
    And the student's email is "student2@example.app"
