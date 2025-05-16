package cat.udl.eps.softarch.tfgfinder.steps;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.springframework.http.MediaType;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentProfileStepDefs {

    private final StepDefs stepDefs;

    public StudentProfileStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }

    @When("I update the student's email to {string}")
    public void i_update_the_students_email_to(String newEmail) throws Exception {
        // Create a JSON object with the new email.
        ObjectNode updateData = stepDefs.mapper.createObjectNode();
        updateData.put("email", newEmail);

        // Obtain the current student's username from the authentication context.
        String username = AuthenticationStepDefs.currentUsername;

        // Perform a PATCH request to update the student's resource.
        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/students/{username}", username)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateData.toString())
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

}
