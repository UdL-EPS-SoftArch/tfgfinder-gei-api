Feature: Edit Category

  Scenario: Edit an existing category as Admin
    Given I login as "admin" with password "password" with role "ROLE_ADMIN" in order to edit a category
    And a category with name "Software Architecture" and description "Topics related to the design and structure of software systems." already exists
    When I edit the category with name "Software Architecture" to new name "Software Design Patterns" and new description "Common solutions to recurring problems in software design."
    Then The response code is 204

  Scenario: Edit an existing category with only new name as Admin
    Given I login as "admin" with password "password" with role "ROLE_ADMIN" in order to edit a category
    And a category with name "Web Development" and description "Frontend and backend technologies." already exists
    When I edit the category with name "Web Development" to new name "Full Stack Development" and no new description
    Then The response code is 204

  Scenario: Attempt to edit a category with empty name as Admin
    Given I login as "admin" with password "password" with role "ROLE_ADMIN" in order to edit a category
    And a category with name "DevOps" and description "Practices for software development and IT operations." already exists
    When I edit the category with name "DevOps" to new name "" and new description "This description should not matter."
    Then The response code is 400

  Scenario: Attempt to edit a category with a name that already exists for another category as Admin
    Given I login as "admin" with password "password" with role "ROLE_ADMIN" in order to edit a category
    And a category with name "Databases" and description "Relational and NoSQL databases" already exists
    And a category with name "Data Structures" and description "Ways to store and organize data" already exists
    When I edit the category with name "Data Structures" to new name "Databases" and new description "Another description for data structures"
    Then The response code is 409

  Scenario: Attempt to edit a non-existent category as Admin
    Given I login as "admin" with password "password" with role "ROLE_ADMIN" in order to edit a category
    When I try to edit the category with name "NonExistentCategory" to new name "New Name" and new description "New description."
    Then The response code is 404

  Scenario: Attempt to edit a category as a normal user
    Given I login as "user" with password "password" with role "ROLE_USER" in order to edit a category
    And a category with name "Data Science" and description "Machine learning and statistics" already exists
    When I try to edit the category with name "Data Science" to new name "AI and Machine Learning" and new description "Advanced topics in AI."
    Then The response code is 403
    And The error message is "Forbidden"
