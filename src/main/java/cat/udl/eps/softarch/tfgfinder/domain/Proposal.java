package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

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



}
