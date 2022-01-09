package eeet2582.realestatemgt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private String gender;
    private String password;
    private Long auth0Id;

    public AppUser(String fullName,
                   String email,
                   String phoneNumber,
                   LocalDate dob,
                   String gender,
                   String password,
                   Long auth0Id) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.gender = gender;
        this.password = password;
        this.auth0Id = auth0Id;
    }
}
