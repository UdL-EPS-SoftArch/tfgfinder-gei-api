Feature: Create a chat and send a message

  Scenario: Create a chat and send a message to a user
    Given There is a registered user with username "user" and password "password" and email "user@demo.app"
    And I login as "user" with password "password"
    And There is a chat
    When I send the message "Hello, how are you?" to the user in the previous chat
    Then the message should be saved correctly with sender "user" and text "Hello, how are you?"

  Scenario: Try to send an empty message
    Given There is a registered user with username "user" and password "password" and email "user@demo.app"
    And I login as "user" with password "password"
    And There is a chat
    When I send the message "" to the user in the previous chat
    Then the message should not be saved

  Scenario: Send a message and verify its timestamp
    Given There is a registered user with username "user" and password "password" and email "user@demo.app"
    And I login as "user" with password "password"
    And There is a chat
    When I send the message "Hello, how are you?" to the user in the previous chat
    Then the message should have a timestamp that is not null

  Scenario: Send a message and verify it is saved with correct sender and timestamp
    Given There is a registered user with username "john" and password "1234" and email "john@example.com"
    And I login as "john" with password "1234"
    And There is a chat
    When I send the message "Test message from John" to the user in the previous chat
    Then the message should be saved correctly with sender "john" and text "Test message from John"
    And the message should have a timestamp that is not null