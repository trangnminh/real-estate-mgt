package eeet2582.realestatemgt.model;

import eeet2582.realestatemgt.helper.UserHouse;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Meeting implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    @Embedded
    private UserHouse userHouse;

    private LocalDate date;
    private LocalTime time;
    private String note;

    public Meeting(UserHouse userHouse, LocalDate date, LocalTime time, String note) {
        this.userHouse = userHouse;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
