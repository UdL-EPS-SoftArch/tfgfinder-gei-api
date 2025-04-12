Feature: Send multiple messages in a chat

  Scenario: Send multiple messages to the user in the chat
    Given a user exists with email "user@example.com" and password "securePassword"
    And I create a new chat
    When I send the message "Hello, how are you?" to the user in the chat
    And I send the message "I'm doing well, thank you!" to the user in the chat
    And I send the message "How about you?" to the user in the chat
    Then the messages should be saved correctly with the texts ["Hello, how are you?", "I'm doing well, thank you!", "How about you?"]