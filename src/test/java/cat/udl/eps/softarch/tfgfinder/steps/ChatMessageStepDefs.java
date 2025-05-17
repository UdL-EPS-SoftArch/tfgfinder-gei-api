package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Chat;
import cat.udl.eps.softarch.tfgfinder.domain.Message;
import cat.udl.eps.softarch.tfgfinder.repository.ChatRepository;
import cat.udl.eps.softarch.tfgfinder.repository.MessageRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@Transactional
public class ChatMessageStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    private Chat chat;
    private Chat secondChat;
    private final Map<Chat, List<String>> chatMessages = new HashMap<>();

    @And("There is a chat")
    public void iCreateANewChat() {
        chat = new Chat();
        chat = chatRepository.save(chat);
    }

    @And("I create a new chat")
    public void iCreateANewChatAgain() {
        if (chat == null) {
            chat = chatRepository.save(new Chat());
        } else {
            secondChat = chatRepository.save(new Chat());
        }
    }

    // Test: Create a chat and send a message to a user
    @When("I send the message {string} to the user in the previous chat")
    public void iSendTheMessage(String text) throws Exception {
        Message sentMessage = new Message();
        sentMessage.setText(text);
        sentMessage.setChat(chat);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(sentMessage))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("the message should be saved correctly with sender {string} and text {string}")
    public void theMessageShouldBeSavedCorrectly(String sender, String expectedText) {
        List<Message> messages = messageRepository.findByChat(chat);
        assertEquals(1, messages.size());
        assertEquals(expectedText, messages.get(0).getText());
        assertEquals(sender, messages.get(0).getFrom().getId());
    }


    @Then("the message should not be saved")
    public void theMessageShouldNotBeSaved() {
        List<Message> messages = messageRepository.findByChat(chat);
        assertTrue(messages.isEmpty());
    }

    // Test: Send multiple messages to the user in the chat
    @Then("the messages should be saved correctly with sender {string} and the texts [{string}, {string}, {string}]")
    public void the_messages_should_be_saved_correctly_with_the_texts(String sender, String text1, String text2, String text3) {
        List<Message> messages = messageRepository.findByChat(chat);
        assertEquals(3, messages.size());
        assertEquals(text1, messages.get(0).getText());
        assertEquals(text2, messages.get(1).getText());
        assertEquals(text3, messages.get(2).getText());

        for (Message message : messages) {
            assertEquals(sender, message.getFrom().getId());
        }
    }

    // Test: The message should have a timestamp that is not null
    @Then("the message should have a timestamp that is not null")
    public void theMessageShouldHaveATimestampThatIsNotNull() {
        List<Message> messages = messageRepository.findByChat(chat);
        assertEquals(1, messages.size());
        assertNotNull(messages.get(0).getWhen());
    }

    // Test: Send messages to different chats and verify separation
    @And("I send the message {string} to the chat")
    public void iSendTheMessageToTheChat(String text) throws Exception {
        Chat targetChat = (secondChat != null && chatMessages.containsKey(chat)) ? secondChat : chat;

        Message sentMessage = new Message();
        sentMessage.setText(text);
        sentMessage.setChat(targetChat);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(sentMessage))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());

        chatMessages.computeIfAbsent(targetChat, c -> new ArrayList<>()).add(text);
    }

    @Then("the first chat should contain 1 message with text {string} from sender {string}")
    public void firstChatContainsMessage(String text, String sender) {
        List<Message> messages = messageRepository.findByChat(chat);
        assertEquals(1, messages.size());
        assertEquals(text, messages.get(0).getText());
        assertEquals(sender, messages.get(0).getFrom().getId());
    }

    @Then("the second chat should contain 1 message with text {string} from sender {string}")
    public void secondChatContainsMessage(String text, String sender) {
        List<Message> messages = messageRepository.findByChat(secondChat);
        assertEquals(1, messages.size());
        assertEquals(text, messages.get(0).getText());
        assertEquals(sender, messages.get(0).getFrom().getId());
    }
}
