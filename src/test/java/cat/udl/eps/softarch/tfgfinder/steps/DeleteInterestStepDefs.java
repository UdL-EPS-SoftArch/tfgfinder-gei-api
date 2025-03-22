package cat.udl.eps.softarch.tfgfinder.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteInterestStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InterestRepository interestRepository;

    private MvcResult result;

    @When("I delete the interest with id {string}")
    public void iDeleteTheInterestWithId(String id) throws Exception {
        result = mockMvc.perform(delete("/interests/" + id))
                .andDo(print())
                .andReturn();

        assertEquals(204, result.getResponse().getStatus());
    }

    @Then("The interest no longer exists")
    public void theInterestNoLongerExists() {
        assertEquals(0, interestRepository.count());
    }
}
