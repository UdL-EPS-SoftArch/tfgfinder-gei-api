Feature: Student Profile Management
  As a student
  I want to update my profile details
  So that my personal information remains current

  Background:
    Given there is a registered student with username "student3" and password "password" and email "student3@example.app"
    And I am not logged in

  Scenario: Update student email
    Given I log in with username "student3" and password "password"
    When I update the student's email to "updated_student3@example.app"
    Then The response code is 200
    And the student's email is "updated_student3@example.app"
