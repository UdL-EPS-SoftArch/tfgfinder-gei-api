package cat.udl.eps.softarch.tfgfinder.config;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class DBInitialization {
    @Value("${default-password}")
    String defaultPassword;
    @Value("${spring.profiles.active:}")
    private String activeProfiles;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

    public DBInitialization(UserRepository userRepository, ProposalRepository proposalRepository) {
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
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
        if (proposalRepository.findByTitle("ZTNA") != null) {
            Proposal proposal = new Proposal();
            proposal.setTitle("ZTNA");
            proposal.setDescription("ZTNA");
            proposal.setTiming("Q1 2025");
            proposal.setSpeciality("Cybersecurity");
            proposal.setKind("Master Thesis");
            proposal.setKeywords("ZTNA, security, networking");
            proposal.setOwner(userRepository.findById("demo").get());
            proposalRepository.save(proposal);
        }
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
