package cat.udl.eps.softarch.tfgfinder.controller;

import cat.udl.eps.softarch.tfgfinder.repository.ExternalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RepositoryRestController
public class ExternalController {

    @Autowired
    private ExternalRepository externalRepository;

    @DeleteMapping("/externals/{id}")
    public ResponseEntity<Void> deleteExternal(@PathVariable String id) {
        if (!externalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        externalRepository.deleteById(id);
        // Retorna 204 No Content
        return ResponseEntity.noContent().build();
    }
}
