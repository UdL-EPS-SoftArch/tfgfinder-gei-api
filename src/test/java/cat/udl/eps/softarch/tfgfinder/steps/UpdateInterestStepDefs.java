package cat.udl.eps.softarch.tfgfinder.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UpdateInterestStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MvcResult result;

    @When("I update the interest with id {string} to status {string}")
    public void iUpdateTheInterestWithIdToStatus(String id, String newStatus) throws Exception {
        Optional<Interest> optionalInterest = interestRepository.findById(Long.parseLong(id));

        if (optionalInterest.isEmpty()) {
            throw new RuntimeException("Interest not found!");
        }

        Interest interest = optionalInterest.get();
        interest.setStatus(newStatus);

        String jsonContent = objectMapper.writeValueAsString(interest);

        result = mockMvc.perform(
                        put("/interests/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    @Then("The interest has been updated with status {string}")
    public void theInterestHasBeenUpdatedWithStatus(String status) {
        Optional<Interest> optionalInterest = interestRepository.findById(Long.parseLong("1"));
        assertTrue(optionalInterest.isPresent(), "Interest not found!");
        Interest updatedInterest = optionalInterest.get();
        assertEquals(status, updatedInterest.getStatus());
    }
}
