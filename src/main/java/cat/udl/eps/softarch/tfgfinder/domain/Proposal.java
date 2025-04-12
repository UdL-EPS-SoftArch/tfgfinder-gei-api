package cat.udl.eps.softarch.tfgfinder.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Proposal extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    @Lob
    private String description;

    @NotBlank
    private String timing;

    @NotBlank
    private String speciality;

    @NotBlank
    private String kind;

    @NotBlank
    private String keywords;

    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @ManyToMany
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Category> categories = new HashSet<>();

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Chat chat;

    @ManyToOne(optional = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Student student;

    @ManyToOne(optional = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Director codirector;

    @ManyToOne(optional = true)
    @JsonIdentityReference(alwaysAsId = true)
    private Professor director;


}






