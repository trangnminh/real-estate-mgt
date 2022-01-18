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
public class Rental implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double depositAmount;
    private Double monthlyFee;
    private Double payableFee;

    @OneToMany(orphanRemoval = true, mappedBy = "rental", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Payment> paymentList;

    public Rental(AppUser user, House house, LocalDate startDate, LocalDate endDate, Double depositAmount, Double monthlyFee, Double payableFee) {
        this.user = user;
        this.house = house;
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositAmount = depositAmount;
        this.monthlyFee = monthlyFee;
        this.payableFee = payableFee;
    }
}
