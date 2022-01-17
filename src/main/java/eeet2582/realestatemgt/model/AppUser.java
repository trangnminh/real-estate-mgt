package eeet2582.realestatemgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class AppUser implements Serializable {

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

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Meeting> meetingList;

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Deposit> depositList;

    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Rental> rentalList;

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
