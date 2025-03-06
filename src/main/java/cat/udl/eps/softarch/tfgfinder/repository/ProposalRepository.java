package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProposalRepository extends CrudRepository<Proposal, Long>, PagingAndSortingRepository<Proposal, Long> {
  //Atributes
  List<Proposal> findByDescriptionContaining(@Param("text") String text);
  Proposal findProposalById(@Param("text")String text);
  Proposal findByChat(Chat chat);
  List<Proposal> findByKind(@Param("text") String kind);
  List<Proposal> findBySpeciality(@Param("text") String text);
  List<Proposal> findByOwner(User user);


  //Relations
  List<Proposal> findByKeywords(@Param("text") String text);
  List<Proposal> findByCategory(Category category);
  List<Proposal> findByTitleContaining(@Param("text") String text);
  List<Proposal> findByStudent(Student student);
  List<Proposal> findByExternal(External external);
  List<Proposal> findByProfessors(Professor professor);
  List<Proposal> findByDirectors(Director director);

}
