package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.*;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import cat.udl.eps.softarch.tfgfinder.repository.StudentRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProfessorRepository;
import cat.udl.eps.softarch.tfgfinder.repository.DirectorRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional  // Ensures test isolation
public class ProposalStepsDefs {
    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    private Proposal proposal;
    private Student student;
    private Professor director;
    private Director codirector;
    private Category category;
    private List<Proposal> searchResults;

    // ---------------- CREATE PROPOSAL ----------------

    @Given("a proposal with title {string}")
    public void a_proposal_with_title(String title) {
        proposal = new Proposal(); // Transient User, not saved
        proposal.setTitle(title);
    }

    @Given("a description {string}")
    public void a_description(String description) {
        proposal.setDescription(description);
    }

    @Given("a timing {string}")
    public void a_timing(String timing) {
        proposal.setTiming(timing);
    }

    @Given("a speciality {string}")
    public void a_speciality(String speciality) {
        proposal.setSpeciality(speciality);
    }

    @Given("a kind {string}")
    public void a_kind(String kind) {
        proposal.setKind(kind);
    }

    @Given("keywords {string}")
    public void keywords(String keywords) {
        proposal.setKeywords(keywords);
    }

    @When("the proposal is saved")
    public void the_proposal_is_saved() throws Throwable {
        // Save the proposal
        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/proposals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(proposal))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
    @Then("the proposal should be stored in the database")
    public void the_proposal_should_be_stored_in_the_database() {
        String location = stepDefs.result.andReturn().getResponse().getHeader("Location");
        try {
            assert location != null;
            stepDefs.result = stepDefs.mockMvc.perform(
                    get(location)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(AuthenticationStepDefs.authenticate()))
                        .andDo(print())
                        .andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // ---------------- RETRIEVE PROPOSAL ----------------

    @When("I search for proposals with title containing {string}")
    public void i_search_for_proposals_with_title_containing(String title) {
        searchResults = proposalRepository.findByTitleContaining(title);
    }

    @Then("I should find at least one proposal")
    public void i_should_find_at_least_one_proposal() {
        assertFalse(searchResults.isEmpty());
    }

    // ---------------- UPDATE PROPOSAL ----------------

    @When("I update the description to {string}")
    public void i_update_the_description_to(String newDescription) {
        proposal.setDescription(newDescription);
        proposal = proposalRepository.save(proposal);
    }

    @Then("the proposal should have the updated description")
    public void the_proposal_should_have_the_updated_description() {
        Optional<Proposal> updatedProposal = proposalRepository.findById(proposal.getId());
        assertTrue(updatedProposal.isPresent());
        assertEquals(updatedProposal.get().getDescription(), proposal.getDescription());
    }

    // ---------------- DELETE PROPOSAL ----------------

    @When("I delete the proposal")
    public void i_delete_the_proposal() {
        proposalRepository.delete(proposal);
    }

    @Then("the proposal should not exist in the database")
    public void the_proposal_should_not_exist_in_the_database() {
        Optional<Proposal> deletedProposal = proposalRepository.findById(proposal.getId());
        assertFalse(deletedProposal.isPresent());
    }

    // ---------------- ASSIGN STUDENT ----------------

    @Given("a student with name {string}")
    public void a_student_with_name(String studentName) {
        student = new Student();
        student.setUsername(studentName);
        student = studentRepository.save(student);
    }

    @When("I assign the proposal to the student")
    public void i_assign_the_proposal_to_the_student() {
        proposal.setStudent(student);
        proposal = proposalRepository.save(proposal);
    }

    @Then("the proposal should have the student assigned")
    public void the_proposal_should_have_the_student_assigned() {
        Proposal savedProposal = proposalRepository.findById(proposal.getId()).orElseThrow();
        assertEquals(student.getId(), savedProposal.getStudent().getId());
    }

    // ---------------- ASSIGN DIRECTOR & CO-DIRECTOR ----------------

    @Given("a professor with name {string}")
    public void a_professor_with_name(String professorName) {
        director = new Professor();
        director.setUsername(professorName);
        director = professorRepository.save(director);
    }

    @Given("a co-director with name {string}")
    public void a_co_director_with_name(String codirectorName) {
        codirector = new Director();
        codirector.setUsername(codirectorName);
        codirector = directorRepository.save(codirector);
    }

    @When("I assign {string} as director")
    public void i_assign_as_director(String professorName) {
        proposal.setDirector(director);
        proposal = proposalRepository.save(proposal);
    }

    @When("I assign {string} as co-director")
    public void i_assign_as_co_director(String codirectorName) {
        proposal.setCodirector(codirector);
        proposal = proposalRepository.save(proposal);
    }

    @Then("the proposal should have both director and co-director assigned")
    public void the_proposal_should_have_both_director_and_co_director_assigned() {
        Proposal savedProposal = proposalRepository.findById(proposal.getId()).orElseThrow();
        assertNotNull(savedProposal.getDirector());
        assertNotNull(savedProposal.getCodirector());
    }

    // ---------------- RETRIEVE BY SPECIALITY ----------------

    @When("I search for proposals with speciality {string}")
    public void i_search_for_proposals_with_speciality(String speciality) {
        searchResults = proposalRepository.findBySpeciality(speciality);
    }

    // ---------------- RETRIEVE BY KEYWORDS ----------------

    @When("I search for proposals containing keyword {string}")
    public void i_search_for_proposals_containing_keyword(String keyword) {
        searchResults = proposalRepository.findByKeywords(keyword);
    }

    // ---------------- VALIDATION CHECK ----------------

    @Given("a proposal with an empty title")
    public void a_proposal_with_an_empty_title() {
        proposal = new Proposal();
        proposal.setTitle(""); // Invalid title
    }

    @When("I try to save the proposal")
    public void i_try_to_save_the_proposal() {
        try {
            proposal = proposalRepository.save(proposal);
        } catch (Exception e) {
            proposal = null;
        }
    }

    @Then("the proposal should not be stored")
    public void the_proposal_should_not_be_stored() {
        assertNull(proposal);
    }

    @Then("I should receive a validation error")
    public void i_should_receive_a_validation_error() {
        assertNull(proposal); // Ensures proposal was not saved due to validation
    }

    // ---------------- ASSIGN CATEGORIES ----------------

    @Given("a category {string}")
    public void a_category(String categoryName) {
        category = new Category();
        category.setName(categoryName);
        category = categoryRepository.save(category);
    }

    @When("I assign the category to the proposal")
    public void i_assign_the_category_to_the_proposal() {
        proposal.getCategories().add(category);
        proposal = proposalRepository.save(proposal);
    }

    @Then("the proposal should have the category assigned")
    public void the_proposal_should_have_the_category_assigned() {
        Proposal savedProposal = proposalRepository.findById(proposal.getId()).orElseThrow();
        assertTrue(savedProposal.getCategories().contains(category));
    }
}
