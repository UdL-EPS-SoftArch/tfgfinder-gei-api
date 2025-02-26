package cat.udl.eps.softarch.tfgfinder.repository;


import cat.udl.eps.softarch.tfgfinder.domain.Invite;
import cat.udl.eps.softarch.tfgfinder.domain.User;
import cat.udl.eps.softarch.tfgfinder.domain.Proposal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;



@RepositoryRestResource
public interface InviteRepository extends CrudRepository<Invite, Long>, PagingAndSortingRepository<Invite, Long> {

    @Query("SELECT i FROM Invite i WHERE i.who = :who")
    List<Invite> findByWho(@Param("who") User who);
    @Query("SELECT i FROM Invite i WHERE i.what = :what")
    List<Invite> findByWhat(@Param("what") Proposal what);

}
