package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Agree;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AgreeRepository extends CrudRepository<User, String>, PagingAndSortingRepository<User, String> {
    @Query("SELECT a.what FROM Agree a WHERE a.what = :proposal")
    List<Proposal> findByProposal(@Param("proposal") Proposal proposal);


    @Query("SELECT a.who FROM Agree a WHERE a.who = :user")
    List<User> findByUser(@Param("user") User user);
}
