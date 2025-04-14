package cat.udl.eps.softarch.tfgfinder.steps;

import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
public class CategoryStepDefs {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category createdCategory;
    private Exception exception;

    @Given("a category named {string} exists")
    public void a_category_named_exists(String name) {
        Category category = new Category();
        category.setName(name);
        category.setDescription("Default description");
        categoryRepository.save(category);
    }

    @Given("a category named {string} with description {string}")
    public void a_category_named_with_description(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryRepository.save(category);
    }

    @When("the admin creates a category with name {string} and description {string}")
    public void the_admin_creates_a_category(String name, String description) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            createdCategory = categoryRepository.save(category);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the category {string} should be available in the system")
    public void the_category_should_be_available(String name) {
        Category found = categoryRepository.findByName(name);
        assertNotNull(found);
    }

    @When("the admin deletes the category {string}")
    public void the_admin_deletes_the_category(String name) {
        Category found = categoryRepository.findByName(name);
        assertNotNull(found);
        categoryRepository.delete(found);
    }

    @Then("the category {string} should not exist in the system")
    public void the_category_should_not_exist(String name) {
        Category found = categoryRepository.findByName(name);
        assertNull(found);
    }

    @Given("a category named {string} with description {string}")
    public void a_category_named_with_description(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        categoryRepository.save(category);
    }

    @When("the admin updates the description to {string}")
    public void the_admin_updates_the_description(String newDescription) {
        if (createdCategory != null) {
            createdCategory.setDescription(newDescription);
            categoryRepository.save(createdCategory);
        }
    }

    @Then("the category {string} should have the description {string}")
    public void the_category_should_have_the_description(String name, String expectedDescription) {
        Category found = categoryRepository.findByName(name);
        assertNotNull(found);
        assertEquals(expectedDescription, found.getDescription());
    }

    @When("the admin tries to create a category with the same name {string}")
    public void the_admin_tries_to_create_duplicate_category(String name) {
        try {
            Category duplicate = new Category();
            duplicate.setName(name);
            duplicate.setDescription("Duplicate attempt");
            categoryRepository.save(duplicate);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("an error should occur indicating the name must be unique")
    public void an_error_should_occur_due_to_duplicate() {
        assertNotNull(exception);
        assertTrue(exception.getMessage().toLowerCase().contains("unique"));
    }
}
