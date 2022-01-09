package eeet2582.realestatemgt.model;

import eeet2582.realestatemgt.helper.UserHouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
    private String date;
    private String time;
    private String note;

    public Deposit(Long userId, Long houseId, Double amount) {
        this.userHouse = new UserHouse(userId, houseId);
        this.amount = amount;
        this.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalDate.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.note = "";
    }

    public Deposit(Long userId, Long houseId, Double amount, String date, String time, String note) {
        this.userHouse = new UserHouse(userId, houseId);
        this.amount = amount;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
