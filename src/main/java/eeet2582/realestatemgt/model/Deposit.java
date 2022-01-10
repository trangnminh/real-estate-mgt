package eeet2582.realestatemgt.model;

import eeet2582.realestatemgt.helper.UserHouse;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositId;

    @Embedded
    private UserHouse userHouse;

    private Double amount;
    private LocalDate date;
    private LocalTime time;
    private String note;

    public Deposit(UserHouse userHouse, Double amount, LocalDate date, LocalTime time, String note) {
        this.userHouse = userHouse;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
