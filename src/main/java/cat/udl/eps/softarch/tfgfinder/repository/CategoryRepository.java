package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepository extends CrudRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {

  Category findByName(@Param("name") String name);

  List<Category> findByNameContaining(@Param("text") String text);

  List<Category> findByNameIgnoreCase(@Param("name") String name);

  List<Category> findByDescriptionContaining(@Param("keyword") String keyword);

  List<Category> findByDescriptionIsNotNull();


  List<Category> findByIdGreaterThan(@Param("id") Long id);
  List<Category> findByIdLessThan(@Param("id") Long id);


  long countByNameContaining(@Param("text") String text);
}
