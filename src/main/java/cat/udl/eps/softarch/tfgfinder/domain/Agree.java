package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class Agree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String status;

    @NotBlank
    private ZonedDateTime when;

    @ManyToOne
    private User who;

    @ManyToOne
    private Proposal what;
}
