package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Director;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@RepositoryRestResource
public interface DirectorRepository extends CrudRepository<Director, String>, PagingAndSortingRepository<Director, String> {

    List<Director> findByNumFinishedProposals(@NotNull int numFinishedProposals);

    List<Director> findByNumActiveProposals(@NotNull int numActiveProposals);
}