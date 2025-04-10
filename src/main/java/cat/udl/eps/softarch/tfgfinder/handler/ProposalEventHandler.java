package cat.udl.eps.softarch.tfgfinder.handler;

import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.repository.ProposalRepository;
import cat.udl.eps.softarch.tfgfinder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterLinkSave;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeLinkSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ProposalEventHandler {

    final Logger logger = LoggerFactory.getLogger(User.class);


    final ProposalRepository proposalRepository;

    public ProposalEventHandler(ProposalRepository proposalRepository) {
        this.proposalRepository = proposalRepository;
    }


    @HandleAfterCreate
    public void handleUserPostCreate(Proposal proposal) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        proposal.setOwner(owner);
    }



}
