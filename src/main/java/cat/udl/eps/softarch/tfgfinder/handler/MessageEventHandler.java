package cat.udl.eps.softarch.tfgfinder.handler;

import cat.udl.eps.softarch.tfgfinder.domain.Message;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class MessageEventHandler {
    final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    @HandleBeforeCreate
    public void handleBeforeCreate(Message message) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setWhen(ZonedDateTime.now());
        message.setFrom(user);
        logger.info("Create new message created: {}", message);
    }
}
