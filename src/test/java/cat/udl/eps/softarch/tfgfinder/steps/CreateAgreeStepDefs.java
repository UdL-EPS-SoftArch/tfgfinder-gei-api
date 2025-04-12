package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.AgreeRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.*;
import java.util.List;

public class CreateAgreeStepDefs {

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
    public void i_select_the_proposal_with_title_to_create_an_agree(String proposalTitle) {
        List<Proposal> proposals = proposalRepository.findByTitleContaining(proposalTitle);
        assertFalse("No proposal with title \"" + proposalTitle + "\" exists", proposals.isEmpty());
        Proposal selectedProposal = proposals.stream()
                .filter(p -> p.getTitle().equals(proposalTitle))
                .findFirst()
                .orElse(null);
        assertNotNull("The proposal with title \"" + proposalTitle + "\" does not exist", selectedProposal);

        newAgree = new Agree();
        newAgree.setWho(this.user);
        newAgree.setWhat(selectedProposal);
        // Assuming your Agree entity has a status field
        newAgree.setStatus("PENDING_INTENT");
        agreeRepository.save(newAgree);
    }

    @Then("A new agree is created between the user {string} and the proposal  with title {string}")
    public void a_new_agree_is_created_between_the_user_and_the_proposal(String username, String proposalTitle) {
        User expectedUser = userRepository.findById(username).orElse(null);
        assertNotNull("Expected user not found: " + username, expectedUser);

        List<Proposal> proposals = proposalRepository.findByTitleContaining(proposalTitle);
        assertFalse("No proposals found with title containing: " + proposalTitle, proposals.isEmpty());

        Proposal expectedProposal = proposals.stream()
                .filter(p -> p.getTitle().equals(proposalTitle))
                .findFirst()
                .orElse(null);
        assertNotNull("Exact proposal with title not found: " + proposalTitle, expectedProposal);


        List<Agree> agreesByUser = agreeRepository.findByWho(expectedUser);
        Agree foundAgree = agreesByUser.stream()
                .filter(a -> {
                    assert a.getWhat().getId() != null;
                    return a.getWhat().getId().equals(expectedProposal.getId());
                })
                .findFirst()
                .orElse(null);

        assertNotNull("No agree was created between user and proposal", foundAgree);
        assertEquals(expectedUser.getId(), foundAgree.getWho().getId());
        assertEquals(expectedProposal.getId(), foundAgree.getWhat().getId());
    }




    @And("^The agree status is \"([^\"]*)\"$")
    public void the_agree_status_is(String status) {
        assertNotNull("The new agree has not been initialized", newAgree);
        // Assuming your Agree entity has a getStatus() method
        assertEquals("The agree status is not the expected one", status, newAgree.getStatus());
    }
}

