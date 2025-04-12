Feature: Create a chat and send a message

  Scenario: Create a chat and send a message to a user
    Given a user exists with email "user@example.com" and password "securePassword"
    And I create a new chat
    When I send the message "Hello, how are you?" to the user in the chat
    Then the message should be saved correctly with the text "Hello, how are you?"

  Scenario: Try to send an empty message
    Given a user exists with email "user@example.com" and password "securePassword"
    And I create a new chat
    When I try to send an empty message to the user in the chat
    Then the message should not be saved

  Scenario: Send a message and verify its timestamp
    Given a user exists with email "user@example.com" and password "securePassword"
    And I create a new chat
    When I send the message "Hello, user!" to the user in the chat
    Then the message should have a timestamp that is not null