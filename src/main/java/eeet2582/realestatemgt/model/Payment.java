package eeet2582.realestatemgt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private LocalDate date;
    private LocalTime time;
    private String note;

    // For reading from JSON file
    public Payment(Rental rental, Double amount, LocalDate date, LocalTime time, String note) {
        this.rental = rental;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    // Using only primitives
    public Payment(Double amount, String date, String time, String note) {
        this.amount = amount;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.note = note;
    }
}
