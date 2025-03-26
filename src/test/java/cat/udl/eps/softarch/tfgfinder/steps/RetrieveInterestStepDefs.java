package cat.udl.eps.softarch.tfgfinder.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class RetrieveInterestStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InterestRepository interestRepository;

    private MvcResult result;

    @When("I retrieve all interests for the user {string}")
    public void iRetrieveAllInterestsForTheUser(String username) throws Exception {
        result = mockMvc.perform(
                        get("/interests/user/" + username))
                .andDo(print())
                .andReturn();
    }

    @Then("The response contains {int} interests")
    public void theResponseContainsInterests(int count) {
        assertEquals(count, interestRepository.count());
    }
}
