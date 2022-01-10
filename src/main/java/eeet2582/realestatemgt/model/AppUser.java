package eeet2582.realestatemgt.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long auth0Id;

    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private String gender;
    private String password;

    public AppUser(Long auth0Id, String fullName, String email, String phoneNumber, LocalDate dob, String gender, String password) {
        this.auth0Id = auth0Id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
        this.password = password;
    }
}
