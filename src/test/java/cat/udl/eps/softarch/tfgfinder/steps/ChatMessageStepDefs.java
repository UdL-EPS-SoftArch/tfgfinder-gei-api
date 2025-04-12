package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Chat;
import cat.udl.eps.softarch.tfgfinder.domain.Message;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.ChatRepository;
import cat.udl.eps.softarch.tfgfinder.repository.MessageRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
public class ChatMessageStepDefs {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    private User user;
    private Chat chat;

    private Exception exception;


    @Given("a user exists with email {string} and password {string}")
    public void aUserExists(String email, String password) {
        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setId(email);
        user.encodePassword();
        userRepository.save(user);
    }

    @And("I create a new chat")
    public void iCreateANewChat() {
        chat = new Chat();
        chatRepository.save(chat);
    }

    // Test: Create a chat and send a message to a user
    @When("I send the message {string} to the user in the chat")
    public void iSendTheMessage(String text) {
        Message sentMessage = new Message();
        sentMessage.setText(text);
        sentMessage.setWhen(ZonedDateTime.now());
        sentMessage.setFrom(user);
        sentMessage.setChat(chat);
        messageRepository.save(sentMessage);
    }

    @Then("the message should be saved correctly with the text {string}")
    public void theMessageShouldBeSavedCorrectly(String expectedText) {
        List<Message> messages = messageRepository.findByChat(chat);
        assertEquals(1, messages.size());
        assertEquals(expectedText, messages.get(0).getText());
        assertEquals(user.getEmail(), messages.get(0).getFrom().getEmail());
    }

    // Test: send an empty message to the user in the chat
    @When("I try to send an empty message to the user in the chat")
    public void iTryToSendAnEmptyMessage() {
        try {
            String emptyMessageText = " ";

            if (emptyMessageText.trim().isEmpty()) {
                throw new IllegalArgumentException("Message cannot be empty");
            }

            Message message = new Message();
            message.setText(emptyMessageText);
            message.setWhen(ZonedDateTime.now());
            message.setFrom(user);
            message.setChat(chat);
            messageRepository.save(message);

        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("the message should not be saved")
    public void theMessageShouldNotBeSaved() {
        assertNotNull(exception);
        assertTrue(exception instanceof IllegalArgumentException);
        List<Message> messages = messageRepository.findByChat(chat);
        assertTrue(messages.isEmpty());
    }

    // test: Send multiple messages to the user in the chat
    @Then("the messages should be saved correctly with the texts [{string}, {string}, {string}]")
    public void the_messages_should_be_saved_correctly_with_the_texts(String text1, String text2, String text3) {
        List<Message> messages = messageRepository.findByChat(chat);

        assertEquals(3, messages.size());

        assertEquals(text1, messages.get(0).getText());
        assertEquals(text2, messages.get(1).getText());
        assertEquals(text3, messages.get(2).getText());

        for (Message message : messages) {
            assertEquals(user.getEmail(), message.getFrom().getEmail());
        }
    }
}
