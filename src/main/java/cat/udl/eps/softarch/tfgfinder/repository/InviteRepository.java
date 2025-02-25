package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Invite;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InviteRepository extends PagingAndSortingRepository<Invite, Long> {
    // Custom query to find invites by status
    List<Invite> findByStatus(String status);
}
