package cat.udl.eps.softarch.tfgfinder.config;
import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import cat.udl.eps.softarch.tfgfinder.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class DBInitialization {
    private final CategoryRepository categoryRepository;
    private final ProposalRepository proposalRepository;
    private final InterestRepository interestRepository;
    @Value("${default-password}")
    String defaultPassword;
    @Value("${spring.profiles.active:}")
    private String activeProfiles;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public DBInitialization(UserRepository userRepository, CategoryRepository categoryRepository, ProposalRepository proposalRepository, 
                            InterestRepository interestRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.proposalRepository = proposalRepository;
        this.interestRepository = interestRepository;
        this.adminRepository = adminRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        // Default user
        if (!userRepository.existsById("demo")) {
            User user = new User();
            user.setEmail("demo@sample.app");
            user.setId("demo");
            user.setPassword(defaultPassword);
            user.encodePassword();
            userRepository.save(user);
        }
        // Default admin
        if (!adminRepository.existsById("admin")) {
            Admin admin = new Admin();
            admin.setEmail("admin@sample.app");
            admin.setId("admin");
            admin.setPassword(defaultPassword);
            admin.encodePassword();
            adminRepository.save(admin);
        }

        Category category = new Category();
        category.setName("Test");
        category.setDescription("Test category");
        category = categoryRepository.save(category);

        Proposal proposal = new Proposal();
        proposal.setTitle("Test proposal");
        proposal.setDescription("Test description");
        proposal.setCategories(Set.of(category));
        proposal.setKeywords("test");
        proposal.setSpeciality("Testing");
        proposal.setTiming("2024-25");
        proposal.setKind("Degree thesis");
        proposal.setOwner(userRepository.findById("demo").orElse(null));
        proposal = proposalRepository.save(proposal);

        Interest interest = new Interest();
        interest.setStatus("Interested");
        interest.setWhat(proposal);
        interest.setWhen(ZonedDateTime.now());
        interest.setWho(userRepository.findById("demo").orElse(null));
        interest = interestRepository.save(interest);

        if (Arrays.asList(activeProfiles.split(",")).contains("test")) {
            // Testing instances
            if (!userRepository.existsById("test")) {
                User user = new User();
                user.setEmail("test@sample.app");
                user.setId("test");
                user.setPassword(defaultPassword);
                user.encodePassword();
                userRepository.save(user);
            }
        }
    }
}
