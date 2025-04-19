package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.External;
import cat.udl.eps.softarch.tfgfinder.repository.ExternalRepository;
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

public class ExternalStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private ExternalRepository externalRepository;

    // Unique step: ensure no external user exists with the given username.
    @Given("there is no registered external with username {string}")
    public void there_is_no_registered_external_with_username(String username) {
        Assert.assertFalse("External \"" + username + "\" should not exist",
                externalRepository.existsById(username));
    }

    // Unique step: register a new external with the company name.
    @When("I register a new external with username {string}, email {string}, password {string}, and company name {string}")
    public void i_register_a_new_external_with_details(String username, String email, String password, String companyName) throws Exception {
        External external = new External();
        external.setId(username);
        external.setEmail(email);
        external.setCompanyName(companyName);
        // Convert the external object to JSON (without the password)
        String externalJson = stepDefs.mapper.writeValueAsString(external);
        // Add the password field.
        JSONObject payload = new JSONObject(externalJson).put("password", password);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/externals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload.toString())
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    // Unique step: verify that the external is created and the password is not returned.
    @Then("an external with username {string} and email {string} has been created, and the password is not returned")
    public void an_external_has_been_created(String username, String email) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/externals/{username}", username)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(email)))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    // Unique step: verify external-specific field.
    @Then("the external user's company name is {string}")
    public void the_externals_company_name_is(String expectedCompany) throws Exception {
        stepDefs.result.andExpect(jsonPath("$.companyName", is(expectedCompany)));
    }

    @Given("there is a registered external with username {string} and password {string} and email {string}, and company name {string}")
    public void there_is_a_registered_external_with_username_and_password_and_email_and_company(String username, String password, String email, String companyName) {
        if (!externalRepository.existsById(username)) {
            External external = new External();
            external.setId(username);
            external.setEmail(email);
            external.setCompanyName(companyName);
            external.setPassword(password);
            external.encodePassword();
            externalRepository.save(external);
        }
    }

}
