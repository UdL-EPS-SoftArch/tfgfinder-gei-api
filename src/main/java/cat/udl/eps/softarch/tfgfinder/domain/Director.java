package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Director extends User{
    private int numFinishedProposals;
    private int numACtiveProposals;
}