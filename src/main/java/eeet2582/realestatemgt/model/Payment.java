package eeet2582.realestatemgt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;

    private Double amount;
    private String date;
    private String time;
    private String note;

    public Payment(Double amount, String note) {
        this.amount = amount;
        this.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.note = note;
    }

    public Payment(Double amount, String date, String time, String note) {
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
