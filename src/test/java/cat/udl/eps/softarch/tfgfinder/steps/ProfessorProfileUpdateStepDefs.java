package cat.udl.eps.softarch.tfgfinder.steps;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProfessorProfileUpdateStepDefs {

    private final StepDefs stepDefs;

    public ProfessorProfileUpdateStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I update the professor's office to {string}")
    public void i_update_the_professors_office_to(String newOffice) throws Exception {
        // Create a JSON object with the updated office.
        ObjectNode updateData = stepDefs.mapper.createObjectNode();
        updateData.put("office", newOffice);

        // Use the current logged-in professor's username.
        String username = AuthenticationStepDefs.currentUsername;
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/professors/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateData.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("the professor's office is {string}")
    public void the_professors_office_is(String expectedOffice) throws Exception {
        stepDefs.result.andExpect(status().isOk())
                .andExpect(jsonPath("$.office", is(expectedOffice)));
    }
}
