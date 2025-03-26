package cat.udl.eps.softarch.tfgfinder.repository;

import cat.udl.eps.softarch.tfgfinder.domain.Professor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProfessorRepository extends CrudRepository<Professor, String>, PagingAndSortingRepository<Professor, String> {

    // Custom query methods for Professor-specific fields
    List<Professor> findByDepartment(@Param("department") String department);

    List<Professor> findByCenter(@Param("center") String center);

    List<Professor> findByOffice(@Param("office") String office);

    // Combined query example
    List<Professor> findByDepartmentAndCenter(@Param("department") String department, @Param("center") String center);
}