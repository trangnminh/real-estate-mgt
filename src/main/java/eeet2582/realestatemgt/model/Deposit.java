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
public class Deposit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    private Double amount;
    private LocalDate date;
    private LocalTime time;
    private String note;

    public Deposit(AppUser user, House house, Double amount, LocalDate date, LocalTime time, String note) {
        this.user = user;
        this.house = house;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
