package eeet2582.realestatemgt.model;

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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    private LocalDate date;
    private LocalTime time;
    private String note;

    public Meeting(AppUser user, House house, LocalDate date, LocalTime time, String note) {
        this.user = user;
        this.house = house;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
