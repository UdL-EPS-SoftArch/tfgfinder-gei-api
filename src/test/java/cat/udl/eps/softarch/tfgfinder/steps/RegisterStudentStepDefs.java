package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Student;
import cat.udl.eps.softarch.tfgfinder.repository.StudentRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegisterStudentStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private StudentRepository studentRepository;

    // ------------------ Common Steps ------------------

    // Step for ensuring that no student exists with the given username.
    @Given("there is no registered student with username {string}")
    public void there_is_no_registered_student_with_username(String username) {
        Assert.assertFalse("Student \"" + username + "\" should not exist",
                studentRepository.existsById(username));
    }

    @Given("there isn't a registered student with username {string}")
    public void there_isnt_registered_student_with_username(String username) {
        Assert.assertFalse("Student \"" + username + "\" should not exist",
                studentRepository.existsById(username));
    }

    // Step for simulating that no user is currently logged in.
    @Given("I am not logged in")
    public void i_am_not_logged_in() {
        // Reset the current authentication credentials.
        AuthenticationStepDefs.currentUsername = null;
        AuthenticationStepDefs.currentPassword = null;
    }

    // Step to create a student in the repository (if not already exists).
    @Given("there is a registered student with username {string} and password {string} and email {string}")
    public void there_is_a_registered_student_with_username_and_password_and_email(String username, String password, String email) {
        if (!studentRepository.existsById(username)) {
            Student student = new Student();
            student.setId(username);
            student.setEmail(email);
            // Set and encode the password before saving.
            student.setPassword(password);
            student.encodePassword();
            studentRepository.save(student);
        }
    }

    // ------------------ Registration ------------------

    // Register a new student via a POST request.
    @When("I register a new student with username {string}, email {string} and password {string}")
    public void i_register_a_new_student_with_username_email_and_password(String username, String email, String password) throws Exception {
        Student student = new Student();
        student.setId(username);
        student.setEmail(email);
        // Convert the Student object to JSON (without the password field yet)
        String studentJson = stepDefs.mapper.writeValueAsString(student);
        // Add the password to the JSON payload.
        JSONObject payload = new JSONObject(studentJson).put("password", password);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload.toString())
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                // Using the authentication helper from your project.
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // A step to verify that the student has been created.
    // This performs a GET on the student's resource and asserts that the email is correct and that the password is not returned.
    @And("a student with username {string} and email {string} has been created, and the password is not returned")
    public void a_student_has_been_created_and_password_not_returned(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/students/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    // ------------------ Login ------------------

    // Log in with a student by sending a GET to /identity (using your existing configuration).
    @When("I log in with username {string} and password {string}")
    public void i_log_in_with_username_and_password(String username, String password) throws Exception {
        AuthenticationStepDefs.currentUsername = username;
        AuthenticationStepDefs.currentPassword = password;

        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/identity")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // Verify that login was successful.
    @And("I can log in with username {string} and password {string}")
    public void i_can_log_in_with_username_and_password(String username, String password) throws Exception {
        // Reuse the login step for assertion.
        i_log_in_with_username_and_password(username, password);
        stepDefs.result.andExpect(status().isOk());
    }

    // ------------------ Retrieve Student Details ------------------

    // Retrieve student details from the repository via a GET call.
    @When("I get the student details for username {string}")
    public void i_get_the_student_details_for_username(String username) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/students/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // Verify that the response contains the expected email and does not include the password.
    @Then("the student's email is {string}")
    public void the_student_email_is(String email) throws Exception {
        stepDefs.result.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
