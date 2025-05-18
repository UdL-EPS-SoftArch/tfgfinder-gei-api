package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.domain.Admin;
import cat.udl.eps.softarch.tfgfinder.repository.AdminRepository;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AddCategoryStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Given("there is a registered admin with username {string} and password {string} and email {string}")
    public void there_is_a_registered_admin_with_username_and_password_and_email(String username, String password, String email) {
        if (!adminRepository.existsById(username)) {
            Admin admin = new Admin();
            admin.setId(username);
            admin.setEmail(email);
            admin.setPassword(password);
            admin.encodePassword();
            adminRepository.save(admin);
        }
    }

    @When("I add a new category with name {string} and description {string}")
    public void iAddANewCategoryWithNameAndDescription(String name, String description) throws Exception {
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setDescription(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(newCategory))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @When("I add a new category with name {string} and no description")
    public void iAddANewCategoryWithNameAndNoDescription(String name) throws Exception {
        Category newCategory = new Category();
        newCategory.setName(name);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(newCategory))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }

    @And("a category with name {string} and description {string} already exists")
    public void aCategoryWithNameAndDescriptionAlreadyExists(String name, String description) {
        Category existingCategory = new Category();
        existingCategory.setName(name);
        existingCategory.setDescription(description);
        categoryRepository.save(existingCategory);
    }

    @When("I try to add a new category with name {string} and description {string}")
    public void iTryToAddANewCategoryWithNameAndDescription(String name, String description) throws Exception {
        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setDescription(description);

        stepDefs.result = stepDefs.mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(newCategory))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(AuthenticationStepDefs.authenticate()))
                .andDo(print());
    }
}