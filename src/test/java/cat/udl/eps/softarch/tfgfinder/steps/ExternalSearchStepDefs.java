package cat.udl.eps.softarch.tfgfinder.steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExternalSearchStepDefs {

    private final StepDefs stepDefs;

    public ExternalSearchStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I search for externals with company name {string}")
    public void i_search_for_externals_with_company_name(String companyName) throws Exception {
        // Assuming the repository is exposed via Spring Data REST and you can filter by companyName using a query parameter.
        stepDefs.result = stepDefs.mockMvc.perform(
                        get("/externals/search/findByCompanyName")
                                .param("companyName", companyName)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @Then("I should see an external with username {string}")
    public void i_should_see_an_external_with_username(String expectedUsername) throws Exception {
        // Assuming the JSON response structure includes an array of externals under _embedded.externals.
        stepDefs.result.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.externals[?(@.username=='" + expectedUsername + "')]").exists());
    }
}
