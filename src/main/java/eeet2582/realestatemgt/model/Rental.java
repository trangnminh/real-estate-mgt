package eeet2582.realestatemgt.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import eeet2582.realestatemgt.helper.UserHouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "rentalId"
)
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Embedded
    private UserHouse userHouse;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double depositAmount;
    private Double monthlyFee;
    private Double payableFee;

    @OneToMany(orphanRemoval = true, mappedBy = "rental")
    private List<Payment> paymentList;
}
