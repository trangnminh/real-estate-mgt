package eeet2582.realestatemgt.model.form;

import eeet2582.realestatemgt.model.helper.ObjectHasUserHouse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DepositForm extends ObjectHasUserHouse {

    private Double amount;
    private String date;
    private String time;
    private String note;
}
