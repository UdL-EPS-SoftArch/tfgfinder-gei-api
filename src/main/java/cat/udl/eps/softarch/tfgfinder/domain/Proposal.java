package cat.udl.eps.softarch.tfgfinder.domain;

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
    private User owner;
    @ManyToMany
    @JoinTable(
            name = "proposal_category",  // Name of the join table
            joinColumns = @JoinColumn(name = "proposal_id"),  // FK to Proposal
            inverseJoinColumns = @JoinColumn(name = "category_id")  // FK to Category
    )
    private Set<Category> categories = new HashSet<>();

    @OneToOne
    private Chat chat;

    @ManyToOne(optional = true)
    private Student student;

    @ManyToOne(optional = true)
    private Director codirector;

    @ManyToOne(optional = true)
    private Professor director;


}






