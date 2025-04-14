Feature: Category management by Admin
  In order to manage categories
  As an admin
  I want to create, delete and edit categories

  Background:
    Given an admin is logged in 
    And a category named "Data Science" exists
    And a proposal with the category "Data Science" exists

  Scenario: Admin creates a new category
    When the admin creates a category with name "Machine Learning" and description "Projects about ML"
    Then the category "Machine Learning" should be available in the system

  Scenario: Admin deletes a category
    Given a category named "Old Category" exists
    When the admin deletes the category "Old Category"
    Then the category "Old Category" should not exist in the system

  Scenario: Admin edits a category description
    Given a category named "AI" with description "Artificial Intelligence"
    When the admin updates the description to "Smart Systems"
    Then the category "AI" should have the description "Smart Systems"

  Scenario: Admin tries to create a duplicate category
    Given a category named "Data Science" already exists
    When the admin tries to create a category with the same name "Data Science"
    Then an error should occur indicating the name must be unique
