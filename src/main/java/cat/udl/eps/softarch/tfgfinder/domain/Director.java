package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Director extends User{

    @NotNull
    private int numFinishedProposals;
    @NotNull
    private int numActiveProposals;
}