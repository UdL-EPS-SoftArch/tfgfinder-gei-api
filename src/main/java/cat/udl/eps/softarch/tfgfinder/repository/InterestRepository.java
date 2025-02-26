package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Interest;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface InterestRepository extends CrudRepository<Interest, Long>, PagingAndSortingRepository<Interest, Long> {

    @Query("SELECT i FROM Invite i WHERE i.who = :who")
    List<Interest> findByWho(@Param("who") User who);
    @Query("SELECT i FROM Invite i WHERE i.what = :what")
    List<Interest> findByWhat(@Param("what") Proposal what);

}
