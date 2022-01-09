package eeet2582.realestatemgt.model;

import eeet2582.realestatemgt.helper.UserHouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    // For reading from JSON file
    public Deposit(UserHouse userHouse, Double amount, LocalDate date, LocalTime time, String note) {
        this.userHouse = userHouse;
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    // Using only primitives
    public Deposit(Long userId, Long houseId, Double amount, String date, String time, String note) {
        this.userHouse = new UserHouse(userId, houseId);
        this.amount = amount;
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.note = note;
    }

    // For "instant" add without date and time
    public Deposit(Long userId, Long houseId, Double amount) {
        this.userHouse = new UserHouse(userId, houseId);
        this.amount = amount;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.note = "";
    }
}
