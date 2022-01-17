package eeet2582.realestatemgt.model.form;

import eeet2582.realestatemgt.model.helper.ObjectHasUserHouse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RentalForm extends ObjectHasUserHouse {

    private String startDate;
    private String endDate;
    private Double depositAmount;
    private Double monthlyFee;
    private Double payableFee;
}
