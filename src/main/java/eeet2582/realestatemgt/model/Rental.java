package eeet2582.realestatemgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eeet2582.realestatemgt.helper.UserHouse;
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
public class Rental implements Serializable {

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

    @OneToMany(orphanRemoval = true, mappedBy = "rental", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Payment> paymentList;

    public Rental(UserHouse userHouse, LocalDate startDate, LocalDate endDate, Double depositAmount, Double monthlyFee, Double payableFee) {
        this.userHouse = userHouse;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositAmount = depositAmount;
        this.monthlyFee = monthlyFee;
        this.payableFee = payableFee;
    }
}
