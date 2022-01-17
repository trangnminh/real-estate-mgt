package eeet2582.realestatemgt.model.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PaymentForm implements Serializable {

    private Double amount;
    private String date;
    private String time;
    private String note;
}
