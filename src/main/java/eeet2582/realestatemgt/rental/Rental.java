package eeet2582.realestatemgt.rental;

import eeet2582.realestatemgt.appuser.AppUser;
import eeet2582.realestatemgt.house.House;
import eeet2582.realestatemgt.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    private Date startDate;
    private Date endDate;
    private Double depositAmount;
    private Double monthlyFee;
    private Double payableFee;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @OneToMany(mappedBy = "rental")
    private List<Payment> paymentList;

    public Rental(Date startDate,
                  Date endDate,
                  Double depositAmount,
                  Double monthlyFee,
                  Double payableFee) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.depositAmount = depositAmount;
        this.monthlyFee = monthlyFee;
        this.payableFee = payableFee;
    }
}
