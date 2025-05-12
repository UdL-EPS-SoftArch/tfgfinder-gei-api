Feature: Send multiple messages in a chat

  Scenario: Send multiple messages to the user in the chat
    Given There is a registered user with username "sender" and password "password" and email "sender@example.com"
    And There is a chat
    And I login as "sender" with password "password"
    When I send the message "Hello, how are you?" to the user in the previous chat
    And I send the message "I'm doing well, thank you!" to the user in the previous chat
    And I send the message "How about you?" to the user in the previous chat
    Then the messages should be saved correctly with sender "sender" and the texts ["Hello, how are you?", "I'm doing well, thank you!", "How about you?"]

  Scenario: Send messages to different chats and verify separation
    Given There is a registered user with username "alice" and password "pass1" and email "alice@example.com"
    And I login as "alice" with password "pass1"
    And I create a new chat
    And I send the message "Message to first chat" to the chat
    And I create a new chat
    And I send the message "Message to second chat" to the chat
    Then the first chat should contain 1 message with text "Message to first chat" from sender "alice"
    And the second chat should contain 1 message with text "Message to second chat" from sender "alice"