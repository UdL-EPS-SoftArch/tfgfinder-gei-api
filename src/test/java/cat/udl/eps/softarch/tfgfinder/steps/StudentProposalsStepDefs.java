package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentProposalsStepDefs {

    private final StepDefs stepDefs;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserRepository userRepository;

    public StudentProposalsStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    /**
     * This step ensures that there exist two proposals with the given titles.
     * It reuses the creation logic (or similar to the one in ProposalStepsDefs) rather than re-implementing it.
     */
    @Given("there exist proposals with titles {string} and {string}")
    public void there_exist_proposals_with_titles_and(String title1, String title2) throws Exception {
        // Create a default dummy owner if needed.
        User owner = userRepository.findById("dummyOwner").orElse(null);
        if (owner == null) {
            owner = new User();
            owner.setId("dummyOwner");
            owner.setEmail("dummy@dummy.com");
            owner.setPassword("password");
            owner.encodePassword();
            userRepository.save(owner);
        }

        // For the first proposal
        if (proposalRepository.findByTitle(title1).isEmpty()) {
            Proposal proposal1 = new Proposal();
            proposal1.setTitle(title1);
            proposal1.setDescription("Default description for " + title1);
            proposal1.setTiming("Full-time");
            proposal1.setSpeciality("General");
            proposal1.setKind("Test");
            proposal1.setKeywords("default");
            proposal1.setOwner(owner);
            proposalRepository.save(proposal1);
        }

        // For the second proposal
        if (proposalRepository.findByTitle(title2).isEmpty()) {
            Proposal proposal2 = new Proposal();
            proposal2.setTitle(title2);
            proposal2.setDescription("Default description for " + title2);
            proposal2.setTiming("Full-time");
            proposal2.setSpeciality("General");
            proposal2.setKind("Test");
            proposal2.setKeywords("default");
            proposal2.setOwner(owner);
            proposalRepository.save(proposal2);
        }
    }

    @When("I list all proposals")
    public void i_list_all_proposals() throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/proposals")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("I should see a proposal with title {string}")
    public void i_should_see_a_proposal_with_title(String expectedTitle) throws Exception {
        // We assume Spring Data REST exposes proposals under "_embedded.proposals"
        stepDefs.result.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.proposals[?(@.title=='" + expectedTitle + "')]").exists());
    }
}
