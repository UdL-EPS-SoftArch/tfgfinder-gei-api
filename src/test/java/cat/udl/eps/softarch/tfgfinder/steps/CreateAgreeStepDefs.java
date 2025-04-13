package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.AgreeRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class CreateAgreeStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private AgreeRepository agreeRepository;

    private User user;
    private Agree newAgree;

    @Given("^A user with username \"([^\"]*)\" exists in the system$")
    public void a_user_with_username_exists_in_the_system(String username) {
        user = userRepository.findById(username).orElse(null);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(username + "@example.com");
            user.setPassword("password");
            user.encodePassword();
            userRepository.save(user);
        }
        assertNotNull("The user with username " + username + " does not exist", user);
    }

    @And("^A proposal with title \"([^\"]*)\" exists in the system$")
    public void a_proposal_with_title_exists_in_the_system(String title) {
        List<Proposal> proposals = proposalRepository.findByTitleContaining(title);

        Proposal proposal;
        if (proposals.isEmpty()) {
            proposal = new Proposal();
            proposal.setTitle(title);
            proposal.setDescription("Descripción de prueba");
            proposal.setOwner(user);
            proposal.setKeywords("AI, ML");
            proposal.setTiming("Second semester");
            proposal.setKind("Research");
            proposal.setSpeciality("Computer Science");
            proposalRepository.save(proposal);
        } else {
            proposal = proposals.stream()
                    .filter(p -> p.getTitle().equals(title))
                    .findFirst()
                    .orElse(null);
        }

        assertNotNull("The proposal with title \"" + title + "\" could not be found or created", proposal);
    }

    @And("^I am logged in as user \"([^\"]*)\"$")
    public void i_am_logged_in_as_user(String username) {
        User loggedInUser = userRepository.findById(username).orElse(null);
        assertNotNull("The logged-in user with username " + username + " does not exist", loggedInUser);
        this.user = loggedInUser;
    }

    @When("^I select the proposal with title \"([^\"]*)\" to create an agree$")
    public void i_select_the_proposal_with_title_to_create_an_agree(String proposalTitle) throws Exception {
        List<Proposal> proposals = proposalRepository.findByTitleContaining(proposalTitle);
        assertFalse("No proposal with title \"" + proposalTitle + "\" exists", proposals.isEmpty());
        Proposal selectedProposal = proposals.stream()
                .filter(p -> p.getTitle().equals(proposalTitle))
                .findFirst()
                .orElse(null);
        assertNotNull("The proposal with title \"" + proposalTitle + "\" does not exist", selectedProposal);

        Map<String, Object> newAgree = new HashMap<>();
        newAgree.put("who", "/users/" + user.getId());
        newAgree.put("what", "/proposals/" + selectedProposal.getId());

        stepDefs.mockMvc.perform(post("/agrees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stepDefs.mapper.writeValueAsString(newAgree))
                        .with(user("student").roles("USER")))
                .andDo(print());
    }

    @Then("A new agree is created between the user {string} and the proposal  with title {string}")
    public void a_new_agree_is_created_between_the_user_and_the_proposal(String username, String proposalTitle) {
        User expectedUser = userRepository.findById(username).orElse(null);
        assertNotNull("Expected user not found: " + username, expectedUser);

        Proposal expectedProposal = proposalRepository.findByTitleContaining(proposalTitle).stream()
                .filter(p -> p.getTitle().equals(proposalTitle))
                .findFirst()
                .orElse(null);
        assertNotNull("Expected proposal not found: " + proposalTitle, expectedProposal);

        // Buscar por usuario y propuesta
        List<Agree> agreesByUser = agreeRepository.findByWho(expectedUser);
        Agree foundAgree = agreesByUser.stream()
                .filter(a -> {
                    assert a.getWhat().getId() != null;
                    return a.getWhat().getId().equals(expectedProposal.getId());
                })
                .findFirst()
                .orElse(null);

        // DEBUG opcional
        System.out.println("Agrees encontrados para el usuario: " + agreesByUser.size());

        assertNotNull("No agree was created between user and proposal", foundAgree);
        assertEquals(expectedUser.getId(), foundAgree.getWho().getId());
        assertEquals(expectedProposal.getId(), foundAgree.getWhat().getId());
    }

    @And("^The agree status is \"([^\"]*)\"$")
    public void the_agree_status_is(String status) {
        List<Agree> agrees = agreeRepository.findByWho(user);
        Agree foundAgree = agrees.stream().findFirst().orElse(null);

        assertNotNull("No agree found to check status", foundAgree);
        assertEquals("The agree status is not the expected one", status, foundAgree.getStatus());
    }
}

