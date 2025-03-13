package cat.udl.eps.softarch.tfgfinder.steps;


import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional  // Ensures test isolation
public class ProposalSteps {

    @Autowired
    private ProposalRepository proposalRepository;

    private Proposal proposal;

    @Given("a proposal with title {string}")
    public void a_proposal_with_title(String title) {
        proposal = new Proposal();
        proposal.setTitle(title);
    }

    @Given("a description {string}")
    public void a_description(String description) {
        proposal.setDescription(description);
    }

    @Given("a timing {string}")
    public void a_timing(String timing) {
        proposal.setTiming(timing);
    }

    @Given("a speciality {string}")
    public void a_speciality(String speciality) {
        proposal.setSpeciality(speciality);
    }

    @Given("a kind {string}")
    public void a_kind(String kind) {
        proposal.setKind(kind);
    }

    @Given("keywords {string}")
    public void keywords(String keywords) {
        proposal.setKeywords(keywords);
    }

    @When("the proposal is saved")
    public void the_proposal_is_saved() {
        proposal = proposalRepository.save(proposal);
    }

    @Then("the proposal should be stored in the database")
    public void the_proposal_should_be_stored_in_the_database() {
        assertNotNull(proposal.getId());
        List<Proposal> foundProposals = proposalRepository.findByTitleContaining(proposal.getTitle());
        assertTrue(!foundProposals.isEmpty());
    }
}
