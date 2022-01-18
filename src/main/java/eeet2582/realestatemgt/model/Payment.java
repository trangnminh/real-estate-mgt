package eeet2582.realestatemgt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    private Double amount;
    private LocalDate date;
    private LocalTime time;
    private String note;

    public Payment(Rental rental, Double amount, LocalDate date, LocalTime time, String note) {
        this.rental = rental;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
