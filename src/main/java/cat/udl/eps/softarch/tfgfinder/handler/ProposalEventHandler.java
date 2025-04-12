package cat.udl.eps.softarch.tfgfinder.handler;

import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ProposalEventHandler {
    final ProposalRepository proposalRepository;

    public ProposalEventHandler(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }

    @HandleBeforeCreate
    public void handleUserPreCreate(Proposal proposal) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        proposal.setOwner(owner);
    }
}
