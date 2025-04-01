package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
