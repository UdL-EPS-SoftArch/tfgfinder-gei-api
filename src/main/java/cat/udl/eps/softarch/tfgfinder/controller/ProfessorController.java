package cat.udl.eps.softarch.tfgfinder.controller;

import cat.udl.eps.softarch.tfgfinder.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RepositoryRestController
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @DeleteMapping("/professors/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable String id) {
        if (!professorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        professorRepository.deleteById(id);
        // Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}
