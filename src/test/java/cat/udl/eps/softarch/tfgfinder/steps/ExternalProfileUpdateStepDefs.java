package cat.udl.eps.softarch.tfgfinder.steps;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ExternalProfileUpdateStepDefs {

    private final StepDefs stepDefs;

    public ExternalProfileUpdateStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I update the external user's company name to {string}")
    public void i_update_the_external_users_company_name_to(String newCompanyName) throws Exception {
        ObjectNode updateData = stepDefs.mapper.createObjectNode();
        updateData.put("companyName", newCompanyName);
        String username = AuthenticationStepDefs.currentUsername;
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/externals/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateData.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}
