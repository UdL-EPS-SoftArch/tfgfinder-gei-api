Feature: Send multiple messages in a chat

  Scenario: Send multiple messages to the user in the chat
    Given There is a registered user with username "sender" and password "password" and email "sender@example.com"
    And There is a chat
    And I login as "sender" with password "password"
    When I send the message "Hello, how are you?" to the user in the previous chat
    And I send the message "I'm doing well, thank you!" to the user in the previous chat
    And I send the message "How about you?" to the user in the previous chat
    Then the messages should be saved correctly with sender "sender" and the texts ["Hello, how are you?", "I'm doing well, thank you!", "How about you?"]
