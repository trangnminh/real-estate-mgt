package eeet2582.realestatemgt.appuser;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private Date dob;
    private String gender;

    public AppUser(String fullName,
                   String email,
                   String password,
                   String phoneNumber,
                   Date dob,
                   String gender) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
    }
}
