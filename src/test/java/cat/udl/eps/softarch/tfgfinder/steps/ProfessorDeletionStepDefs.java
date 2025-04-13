package cat.udl.eps.softarch.tfgfinder.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfessorDeletionStepDefs {

    private final StepDefs stepDefs;

    public ProfessorDeletionStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I delete the professor with username {string}")
    public void i_delete_the_professor_with_username(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/professors/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("a GET request for professor with username {string} returns a {int} error")
    public void a_get_request_for_professor_returns_a_error(String username, int expectedStatus) throws Exception {
        // Utilitzem credencials d'un usuari dummy, per exemple "demo", que sabem que existeix.
        AuthenticationStepDefs.currentUsername = "demo";
        AuthenticationStepDefs.currentPassword = "password"; // assegura't que "demo" està registrat.

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/professors/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().is(expectedStatus));
    }

}
