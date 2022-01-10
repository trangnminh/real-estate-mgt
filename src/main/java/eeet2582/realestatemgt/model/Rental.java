package eeet2582.realestatemgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eeet2582.realestatemgt.helper.UserHouse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private List<Payment> paymentList;

    // For reading from JSON file
    public Rental(UserHouse userHouse, LocalDate startDate, LocalDate endDate, Double depositAmount, Double monthlyFee, Double payableFee) {
        this.userHouse = userHouse;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositAmount = depositAmount;
        this.monthlyFee = monthlyFee;
        this.payableFee = payableFee;
    }

    // Using only primitives
    public Rental(Long userId, Long houseId, String startDate, String endDate, Double depositAmount, Double monthlyFee, Double payableFee) {
        this.userHouse = new UserHouse(userId, houseId);
        this.startDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.depositAmount = depositAmount;
        this.monthlyFee = monthlyFee;
        this.payableFee = payableFee;
    }
}
