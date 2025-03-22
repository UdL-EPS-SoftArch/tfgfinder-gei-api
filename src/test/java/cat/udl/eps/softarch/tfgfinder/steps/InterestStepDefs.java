package cat.udl.eps.softarch.tfgfinder.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

@SpringBootTest
@AutoConfigureMockMvc
public class InterestStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult result;

    @Given("There is an existing user with username {string}")
    public void thereIsAnExistingUserWithUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail(username + "@example.com");
        userRepository.save(user);
    }

    @Given("There is an existing proposal with title {string}")
    public void thereIsAnExistingProposalWithTitle(String title) {
        Proposal proposal = new Proposal();
        proposal.setTitle(title);
        proposal.setDescription("Sample description");
        proposal.setTiming("2025-06-01");
        proposal.setSpeciality("Computer Science");
        proposal.setKind("Research");
        proposal.setKeywords("AI, ML"); // Fixed single string instead of array
        proposal.setOwner(new User()); // Assigning a default owner
        proposalRepository.save(proposal);
    }

    @When("I create a new interest with status {string} and timestamp {string} for user {string} and proposal {string}")
    public void iCreateANewInterest(String status, String timestamp, String username, String proposalTitle) throws Exception {
        Interest interest = new Interest();
        interest.setStatus(status);
        interest.setWhen(ZonedDateTime.parse(timestamp));
        interest.setWho(new User()); // Assign a default user
        interest.setWhat(new Proposal()); // Assign a default proposal

        String jsonContent = objectMapper.writeValueAsString(interest);

        result = mockMvc.perform(
                        post("/interests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andReturn();
    }

    @Then("The response code is {int}")
    public void theResponseCodeIs(int expectedCode) {
        assertEquals(expectedCode, result.getResponse().getStatus());
    }

    @Then("The interest has been created with status {string}")
    public void theInterestHasBeenCreatedWithStatus(String status) {
        assertEquals(1, interestRepository.count());
    }

    @Then("It has not been created an interest")
    public void itHasNotBeenCreatedAnInterest() {
        assertEquals(0, interestRepository.count());
    }
}
