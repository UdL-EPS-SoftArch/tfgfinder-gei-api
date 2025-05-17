package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.AgreeRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RejectOfferStepDefs{

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private AgreeRepository agreeRepository;

    private String currentUsername;
    private String currentPassword;
    private String[] currentRoles;


    @Given("I login as {string} with password {string} with role {string}")
    public void iLoginAsWithPasswordWithRole(String username, String password, String role) {
        this.currentUsername = username;
        this.currentPassword = password;
        this.currentRoles = new String[]{role};
    }

    private RequestPostProcessor getAuthenticationRequestPostProcessor() {
        if (currentUsername == null || currentPassword == null || currentRoles == null) {
            throw new IllegalStateException("username or password or role/s can't be null");
        }
        List<SimpleGrantedAuthority> authorities = Arrays.stream(currentRoles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return SecurityMockMvcRequestPostProcessors.user(currentUsername)
                .password(currentPassword)
                .authorities(authorities);
    }


    @Given("^An agreement exists between user \"([^\"]*)\" and proposal with title \"([^\"]*)\"$")
    public void anAgreementExists(String username, String proposalTitle) {
        User user = userRepository.findById(username).orElseThrow();
        Proposal proposal = proposalRepository.findByTitle(proposalTitle).stream().findFirst().orElse(null);
        Agree agree = new Agree();
        agree.setWho(user);
        agree.setWhen(agree.getWhen());
        agree.setWhat(proposal);
        agree.setStatus("CREATED");
        agreeRepository.save(agree);
    }

    @When("^I try to delete the offer \"([^\"]*)\" without authentication$")
    public void iRejectTheAgreement(String proposalTitle) throws Exception {
        Proposal proposal = proposalRepository.findByTitle(proposalTitle).stream().findFirst().orElse(null);
        assert proposal != null;
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/proposals/{id}", proposal.getId()) // sin autenticación
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());
    }

    @When("^I delete the offer with title \"([^\"]*)\" by the user \"([^\"]*)\"$")
    public void iDeleteTheOfferWithTitleByTheUser(String proposalTitle, String username) throws Exception {
        User user = userRepository.findById(username).orElseThrow();
        Proposal proposal = proposalRepository.findByTitle(proposalTitle).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("Review with title " + proposalTitle + " not found"));
        Agree agree = (Agree) agreeRepository.findByWhoAndWhat(user, proposal).stream().findFirst().orElseThrow(() ->
                new IllegalArgumentException("Review with title " + proposalTitle + "or" + username + " not found"));

        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/agrees/{id}", agree.getId())
                        .with(getAuthenticationRequestPostProcessor())
        ).andDo(print());
        stepDefs.result = stepDefs.mockMvc.perform(
                delete("/proposals/{id}", proposal.getId())
                        .with(getAuthenticationRequestPostProcessor())
        ).andDo(print());
    }

    @When ("^I delete the offer with id \"([^\"]*)\"$")
    public void iDeleteTheOfferWithId(Long id) throws Exception {
        stepDefs.result = stepDefs.mockMvc.perform(
                        delete("/proposals/{id}", id)
                                .with(getAuthenticationRequestPostProcessor()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}