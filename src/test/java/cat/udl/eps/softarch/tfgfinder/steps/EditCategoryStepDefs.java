package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

public class EditCategoryStepDefs {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private CategoryRepository categoryRepository;

    private String currentUsername;
    private String currentPassword;
    private String[] currentRoles;

    @Given("I login as {string} with password {string} with role {string} in order to edit a category")
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

    @When("I edit the category with name {string} to new name {string} and new description {string}")
    public void iEditTheCategoryWithNewNameAndNewDescription(String oldName, String newName, String newDescription) throws Exception {
        Optional<Category> categoryToUpdate = Optional.ofNullable(categoryRepository.findByName(oldName));
        assertTrue(categoryToUpdate.isPresent());
        Category updatedCategory = categoryToUpdate.get();
        updatedCategory.setName(newName);
        updatedCategory.setDescription(newDescription);

        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/categories/" + updatedCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(updatedCategory))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(getAuthenticationRequestPostProcessor()))
                .andDo(MockMvcResultHandlers.print());
    }

    @When("I edit the category with name {string} to new name {string} and no new description")
    public void iEditTheCategoryWithNewNameAndNoNewDescription(String oldName, String newName) throws Exception {
        Optional<Category> categoryToUpdate = Optional.ofNullable(categoryRepository.findByName(oldName));
        assertTrue(categoryToUpdate.isPresent());
        Category updatedCategory = categoryToUpdate.get();
        updatedCategory.setName(newName);
        updatedCategory.setDescription(null);

        stepDefs.result = stepDefs.mockMvc.perform(
                        patch("/categories/" + updatedCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stepDefs.mapper.writeValueAsString(updatedCategory))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .with(getAuthenticationRequestPostProcessor()))
                .andDo(MockMvcResultHandlers.print());
    }

    @When("I try to edit the category with name {string} to new name {string} and new description {string}")
    public void iTryToEditTheCategoryWithNewNameAndNewDescription(String oldName, String newName, String newDescription) throws Exception {
        Optional<Category> categoryOptional = Optional.ofNullable(categoryRepository.findByName(oldName));

        Category updatedCategory = new Category();
        updatedCategory.setName(newName);
        updatedCategory.setDescription(newDescription);

        if (categoryOptional.isPresent()) {
            Category existingCategory = categoryOptional.get();
            stepDefs.result = stepDefs.mockMvc.perform(
                            patch("/categories/" + existingCategory.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(stepDefs.mapper.writeValueAsString(updatedCategory))
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .with(getAuthenticationRequestPostProcessor()))
                    .andDo(MockMvcResultHandlers.print());
        }else {
            stepDefs.result = stepDefs.mockMvc.perform(
                            patch("/categories/99999") //Category 99999 we know that don't exist
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(stepDefs.mapper.writeValueAsString(updatedCategory))
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .with(getAuthenticationRequestPostProcessor()))
                    .andDo(MockMvcResultHandlers.print());
        }
    }
}
