package eeet2582.realestatemgt.model;

import eeet2582.realestatemgt.helper.UserHouse;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    @Embedded
    private UserHouse userHouse;

    private String date;
    private String time;
    private String note;

    public Meeting(UserHouse userHouse, String date, String time, String note) {
        this.userHouse = userHouse;
        this.date = date;
        this.time = time;
        this.note = note;
    }
}
