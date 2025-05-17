package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors; // Importar
import org.springframework.test.web.servlet.request.RequestPostProcessor; // Importar

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AddCategoryStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private CategoryRepository categoryRepository;

    private String currentUsername;
    private String currentPassword;
    private String[] currentRoles; //Maybe the user has more than one role, is a good practice make this array in this case.

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
                                .with(getAuthenticationRequestPostProcessor()))
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
                                .with(getAuthenticationRequestPostProcessor())
                )
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
                                .with(getAuthenticationRequestPostProcessor()))
                .andDo(print());
    }
}