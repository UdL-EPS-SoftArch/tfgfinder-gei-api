package cat.udl.eps.softarch.tfgfinder.handler;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.ZonedDateTime;

@RepositoryEventHandler
public class AgreeEventHandler {
    final Logger logger = LoggerFactory.getLogger(Agree.class);

    @HandleBeforeCreate
    public void handleBeforeCreate(Agree agree) {
        agree.setWhen(ZonedDateTime.now());
    }

    @HandleBeforeSave
    public void handleBeforeSave(Agree agree) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User agreeOwner = agree.getWho();
        assert agreeOwner != null;
        assert agreeOwner.getId() != null;
        if(!agreeOwner.getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to perform this action");
        }
        logger.info("New agreement updated: {}", agree);
    }

}
