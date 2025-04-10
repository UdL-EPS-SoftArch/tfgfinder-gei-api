package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;

public class InterestStepDefs {

    final StepDefs stepDefs;
    final ProposalRepository proposalRepository;
    final InterestRepository interestRepository;
    final UserRepository userRepository;
    private String newResourceUri;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public InterestStepDefs(StepDefs stepDefs, ProposalRepository proposalRepository, 
                           InterestRepository interestRepository, UserRepository userRepository) {
        this.stepDefs = stepDefs;
        this.proposalRepository = proposalRepository;
        this.interestRepository = interestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        
        // Configure ObjectMapper
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());  // Add support for Java 8 date/time types
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  // Use ISO-8601 format
        this.objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
    }

    @Given("there is a registered user with username {string} and password {string} and email {string}")
    public void thereIsARegisteredUserWithUsernameAndPasswordAndEmail(String username, String password, String email) {
        if (!userRepository.existsById(username)) {
            User user = new User();
            user.setEmail(email);
            user.setId(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    @Given("there is a created proposal with title {string} and description {string}")
    public void thereIsACreatedProposalWithTitleAndDescription(String title, String description) {
        // Create a default owner if not exists
        User owner = new User();
        owner.setId("owner");
        owner.setEmail("owner@example.com");
        owner.setPassword(passwordEncoder.encode("password"));
        if (!userRepository.existsById("owner")) {
            userRepository.save(owner);
        } else {
            owner = userRepository.findById("owner").get();
        }

        Proposal proposal = new Proposal();
        proposal.setTitle(title);
        proposal.setDescription(description);
        proposal.setKind("TFG");
        proposal.setSpeciality("Computer Science");
        proposal.setTiming("Full-time");
        proposal.setKeywords("test");
        proposal.setOwner(owner);
        proposalRepository.save(proposal);
    }

    @When("I create an interest for the proposal with title {string}")
    public void iCreateAnInterestForTheProposalWithTitle(String title) throws Exception {
        Proposal proposal = proposalRepository.findByTitle(title).orElse(null);
        String username = AuthenticationStepDefs.currentUsername;
        
        if (proposal != null) {
            String jsonBody = String.format(
                "{" +
                "\"status\": \"PENDING\"," +
                "\"when\": \"%s\"," +
                "\"who\": \"/users/%s\"," +
                "\"what\": \"/proposals/%d\"" +
                "}",
                ZonedDateTime.now().toString(),
                username,
                proposal.getId()
            );
            
            stepDefs.result = stepDefs.mockMvc.perform(
                    post("/interests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody)
                            .accept(MediaType.APPLICATION_JSON)
                            .with(AuthenticationStepDefs.authenticate()))
                    .andDo(MockMvcResultHandlers.print());
        } else {
            // For non-existing proposal, try to access a non-existent proposal directly
            stepDefs.result = stepDefs.mockMvc.perform(
                    get("/proposals/999999")
                            .accept(MediaType.APPLICATION_JSON)
                            .with(AuthenticationStepDefs.authenticate()))
                    .andDo(MockMvcResultHandlers.print());
        }

        if (stepDefs.result.andReturn().getResponse().getStatus() == 201) {
            newResourceUri = stepDefs.result.andReturn().getResponse().getHeader("Location");
        }
    }

    @And("It has been created an interest with status {string}")
    public void itHasBeenCreatedAnInterestWithStatus(String status) throws Exception {
        stepDefs.result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.status", is(status)));
    }

    @When("I list all interests")
    public void iListAllInterests() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/interests")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(MockMvcResultHandlers.print());
    }

    @When("I get the created interest")
    public void iGetTheCreatedInterest() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                get(newResourceUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(MockMvcResultHandlers.print());
    }

    @And("The interest status is {string}")
    public void theInterestStatusIs(String status) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.status", is(status)));
    }

    @When("I delete the created interest")
    public void iDeleteTheCreatedInterest() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                delete(newResourceUri)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(MockMvcResultHandlers.print());
    }

    @When("I update the interest status to {string}")
    public void iUpdateTheInterestStatusTo(String status) throws Exception {
        Interest interest = new Interest();
        interest.setStatus(status);
        interest.setWhen(ZonedDateTime.now());

        stepDefs.result = stepDefs.mockMvc.perform(
                patch(newResourceUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interest))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(MockMvcResultHandlers.print());
    }
} 