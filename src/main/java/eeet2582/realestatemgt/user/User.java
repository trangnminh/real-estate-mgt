package eeet2582.realestatemgt.user;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date dob;
    private String gender;

    public User(String fullName, String email, String password, String phoneNumber, Date dob, String gender) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
    }
}
