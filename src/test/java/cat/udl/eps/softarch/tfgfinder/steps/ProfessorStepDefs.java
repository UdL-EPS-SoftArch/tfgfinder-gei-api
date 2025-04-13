package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Professor;
import cat.udl.eps.softarch.tfgfinder.repository.ProfessorRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProfessorStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private ProfessorRepository professorRepository;

    // Unique step: ensure no professor exists with the given username.
    @Given("there is no registered professor with username {string}")
    public void there_is_no_registered_professor_with_username(String username) {
        Assert.assertFalse("Professor \"" + username + "\" should not exist",
                professorRepository.existsById(username));
    }

    // Unique step: register a new professor with extra fields.
    @When("I register a new professor with username {string}, email {string}, password {string}, department {string}, center {string}, and office {string}")
    public void i_register_a_new_professor_with_details(String username, String email, String password, String department, String center, String office) throws Exception {
        Professor professor = new Professor();
        professor.setId(username);
        professor.setEmail(email);
        professor.setDepartment(department);
        professor.setCenter(center);
        professor.setOffice(office);
        // Convert the professor object to JSON (without the password)
        String professorJson = stepDefs.mapper.writeValueAsString(professor);
        // Add the password field to the JSON payload.
        JSONObject payload = new JSONObject(professorJson).put("password", password);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/professors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload.toString())
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // Unique step: verify that the professor is created and the password is not returned.
    @Then("a professor with username {string} and email {string} has been created, and the password is not returned")
    public void a_professor_has_been_created(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/professors/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    // Unique step: verify professor-specific field.
    @Then("the professor's department is {string}")
    public void the_professors_department_is(String expectedDepartment) throws Exception {
        // Assumes that the current response contains the professor details.
        stepDefs.result.andExpect(jsonPath("$.department", is(expectedDepartment)));
    }
    @Given("there is a registered professor with username {string} and password {string} and email {string}")
    public void there_is_a_registered_professor_with_username_and_password_and_email(String username, String password, String email) {
        if (!professorRepository.existsById(username)) {
            Professor professor = new Professor();
            professor.setId(username);
            professor.setEmail(email);
            professor.setPassword(password);
            professor.encodePassword();
            // Optionally, set default values for professor-specific fields if needed.
            professorRepository.save(professor);
        }
    }
}
