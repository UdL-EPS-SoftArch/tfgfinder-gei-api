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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateInterestStepDefs {

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
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword("password");
            user.setEmail(username + "@example.com");
            userRepository.save(user);
        }
    }

    @Given("There is an existing proposal with title {string}")
    public void thereIsAnExistingProposalWithTitle(String title) {
        List<Proposal> proposals = proposalRepository.findAll();
        if (proposals.isEmpty()) {
            List<User> users = (List<User>) userRepository.findAll();
            User owner = users.isEmpty() ? new User() : users.get(0);

            if (owner.getUsername() == null) {
                owner.setUsername("default_owner");
                owner.setPassword("password");
                owner.setEmail("default_owner@example.com");
                userRepository.save(owner);
            }

            Proposal proposal = new Proposal();
            proposal.setTitle(title);
            proposal.setDescription("Sample proposal description");
            proposal.setTiming("2025-06-01");
            proposal.setSpeciality("Computer Science");
            proposal.setKind("Research");
            proposal.setKeywords("AI, ML");
            proposal.setOwner(owner);

            proposalRepository.save(proposal);
        }
    }

    @When("I create a new interest with status {string} and timestamp {string} for user {string} and proposal {string}")
    public void iCreateANewInterest(String status, String timestamp, String username, String proposalTitle) throws Exception {
        List<User> users = (List<User>) userRepository.findAll();
        List<Proposal> proposals = proposalRepository.findAll();

        if (users.isEmpty() || proposals.isEmpty()) {
            throw new RuntimeException("User or Proposal not found.");
        }

        Interest interest = new Interest();
        interest.setStatus(status);
        interest.setWhen(ZonedDateTime.parse(timestamp));
        interest.setWho(users.get(0));
        interest.setWhat(proposals.get(0));

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
}
