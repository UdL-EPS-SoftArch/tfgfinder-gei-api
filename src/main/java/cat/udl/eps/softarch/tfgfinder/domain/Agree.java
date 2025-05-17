package cat.udl.eps.softarch.tfgfinder.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;


@Entity
@Data
public class Agree {

    @NotBlank
    private enum InternalStatus{
        CREATED,
        ACCEPTED,
        REJECTED,
        COMPLETED
    }
    private InternalStatus currentStatus = InternalStatus.CREATED;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    public String getStatus(){
        return currentStatus.toString();
    }
    public void setStatus(String status) {
        this.currentStatus = InternalStatus.valueOf(status);
    }


    @NotNull
    private ZonedDateTime when = ZonedDateTime.now();;

    @ManyToOne
    private User who;

    @ManyToOne
    private Proposal what;
}
