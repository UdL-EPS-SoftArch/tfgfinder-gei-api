package cat.udl.eps.softarch.tfgfinder.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExternalDeletionStepDefs {

    private final StepDefs stepDefs;

    public ExternalDeletionStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I delete the external with username {string}")
    public void i_delete_the_external_with_username(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/externals/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("a GET request for external with username {string} returns a {int} error")
    public void a_get_request_for_external_returns_a_error(String username, int expectedStatus) throws Exception {
        // Utilitzem un usuari vàlid per fer la petició GET, per exemple "demo"
        AuthenticationStepDefs.currentUsername = "demo";
        AuthenticationStepDefs.currentPassword = "password";

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/externals/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().is(expectedStatus));
    }

}
