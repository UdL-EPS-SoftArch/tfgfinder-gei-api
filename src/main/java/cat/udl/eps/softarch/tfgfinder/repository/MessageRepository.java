package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Chat;
import cat.udl.eps.softarch.tfgfinder.domain.Invite;
import cat.udl.eps.softarch.tfgfinder.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long>, PagingAndSortingRepository<Message, Long> {
    List<Message> findByChat(Chat chat);
}