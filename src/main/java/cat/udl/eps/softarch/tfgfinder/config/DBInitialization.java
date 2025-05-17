package cat.udl.eps.softarch.tfgfinder.config;
import cat.udl.eps.softarch.tfgfinder.domain.Category;
import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.CategoryRepository;
import cat.udl.eps.softarch.tfgfinder.repository.InterestRepository;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
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

    public DBInitialization(UserRepository userRepository, CategoryRepository categoryRepository, ProposalRepository proposalRepository, InterestRepository interestRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.proposalRepository = proposalRepository;
        this.interestRepository = interestRepository;
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
        if (proposalRepository.findByTitle("ZTNA") != null) {
            Proposal proposal2 = new Proposal();
            proposal.setTitle("ZTNA");
            proposal.setDescription("ZTNA");
            proposal.setTiming("Q1 2025");
            proposal.setSpeciality("Cybersecurity");
            proposal.setKind("Master Thesis");
            proposal.setKeywords("ZTNA, security, networking");
            proposal.setOwner(userRepository.findById("demo").get());
            proposalRepository.save(proposal2);
        }
    }
}




