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
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ModifyAgreeStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private AgreeRepository agreeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    private MvcResult result;


    @Given("There is an agreement with title {string} and status {string}")
    public void thereIsAnAgreement(String title, String status) {
        User student = new User();
        student.setUsername("student");
        student.setEmail("student@example.com");
        student.setPassword("password");
        student.encodePassword();
        student = userRepository.save(student);

        Proposal proposal = new Proposal();
        proposal.setTitle(title);
        proposal.setOwner(student);
        proposal.setTiming("10/04/2025");
        proposal.setKind("proposal");
        proposal.setKeywords("test proposal");
        proposal.setDescription("new test proposal");
        proposal.setSpeciality("IT");
        proposal = proposalRepository.save(proposal);

        Agree agree = new Agree();
        agree.setWho(student);
        agree.setWhat(proposal);
        agree.setStatus(status);
        agree.setWhen(agree.getWhen());
        agreeRepository.save(agree);
    }

    @When("I want to modify the agreement with title {string} with the new status {string}")
    public void iWantToModifyTheAgreementWithIdWithTheNewStatus(String title, String newStatus) throws Exception {
        Proposal proposal = proposalRepository.findByTitle(title).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("Proposal with title " + title + " not found"));
        Agree agree = agreeRepository.findByWhat(proposal).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("Agree with title " + title + "or" + newStatus + " not found"));
        agree.setStatus(newStatus);

        result = stepDefs.mockMvc.perform(patch("/agrees/" + agree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"" + newStatus + "\"}")
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

    }

    @Then("The state has been successfully modified")
    public void theStateHasBeenSuccessfullyModified() {
        assertNotNull(result, "The state should not be null after modification");
        Assertions.assertEquals(204, result.getResponse().getStatus(), "Expected HTTP status code 204");
    }

    @And("I want to check an agreement with the id \"([^\"]*)\"$")
    public void iWantToCheckAnAgreementWithTheId(Long agreementId) throws Exception {
        Optional<Agree> opt = agreeRepository.findById(agreementId);


    }


}