package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.repository.AgreeRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;

@Component
public class ModifyAgreeStepDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private AgreeRepository agreeRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    private MvcResult result;

    @When("I want to modify the agreement with id {long} with the new status {string}")
    public void modifyAgreementWithIdWithTheNewStatus(Long agreementId, String newStatus) throws Exception {
        Proposal proposal = proposalRepository.findProposalById(agreementId);
        Agree agree = agreeRepository.findByWhat(proposal).get(0);
        agree.setStatus(newStatus);

        result = stepDefs.mockMvc.perform(put("/agrees/" + agree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"when\": \"" + newStatus + "\"}")
                        .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();


    }
    @Then("The state has been successfully modified")
    public void theStateHasBeenSuccessfullyModified() {
        assertNotNull(result, "The state should not be null after modification");
        Assertions.assertEquals(204, result.getResponse().getStatus(), "Expected HTTP status code 200");
    }
}