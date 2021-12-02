package eeet2582.realestatemgt.payment;

import eeet2582.realestatemgt.rental.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Double amount;
    private Date date;
    private String note;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    public Payment(Double amount, Date date, String note) {
        this.amount = amount;
        this.date = date;
        this.note = note;
    }
}
