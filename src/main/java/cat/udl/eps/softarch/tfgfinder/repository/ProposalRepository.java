package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProposalRepository extends PagingAndSortingRepository<Proposal, Long> {

  // Attributes
  List<Proposal> findByDescriptionContaining(String text);
  Proposal findProposalById(Long id);  // Fixed parameter type
  Proposal findByChat(Chat chat);
  List<Proposal> findByKind(String kind);
  List<Proposal> findBySpeciality(String text);
  List<Proposal> findByOwner(User user);

  // Relations
  List<Proposal> findByKeywords(String text);
  List<Proposal> findByCategories(Category category); // Fixed: categories is a Set<Category>
  List<Proposal> findByTitleContaining(String text);
  List<Proposal> findByStudent(Student student);

  List<Proposal> findByDirector(Professor Director);
  List<Proposal> findByCodirector(Director Codirector);
}