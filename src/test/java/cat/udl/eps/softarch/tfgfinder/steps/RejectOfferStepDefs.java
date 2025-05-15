package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.AgreeRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RejectOfferStepDefs{

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private AgreeRepository agreeRepository;

    @Autowired
    private User loggedUser;

    @Given("^An agreement exists between user \"([^\"]*)\" and proposal with title \"([^\"]*)\"$")
    public void anAgreementExists(String username, String proposalTitle) {
        User user = userRepository.findById(username).orElseThrow();
        Proposal proposal = proposalRepository.findByTitle(proposalTitle).stream().findFirst().orElse(null);
        Agree agree = new Agree();
        agree.setWho(user);
        agree.setWhat(proposal);
        agree.setStatus("CREATED");
        agreeRepository.save(agree);
    }

    @When("^I reject the agreement between user \"([^\"]*)\" and proposal with title \"([^\"]*)\"$")
    public void iRejectTheAgreement(String username, String proposalTitle) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(put("/agreements/reject")
                        .param("username", username)
                        .param("proposalTitle", proposalTitle)
                        .with(user(loggedUser.getUsername()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
}
