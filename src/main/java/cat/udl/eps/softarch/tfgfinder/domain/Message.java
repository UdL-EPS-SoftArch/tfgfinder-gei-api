package cat.udl.eps.softarch.tfgfinder.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Table(name = "Message")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String text;

    @NotNull
    private ZonedDateTime when;

    @ManyToOne
    private User from;
}


