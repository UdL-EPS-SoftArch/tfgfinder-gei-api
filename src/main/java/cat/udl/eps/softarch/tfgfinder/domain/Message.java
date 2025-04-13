package cat.udl.eps.softarch.tfgfinder.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    @NotNull
    private ZonedDateTime when;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User from;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Chat chat;
}
