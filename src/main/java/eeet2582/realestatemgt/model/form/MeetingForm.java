package eeet2582.realestatemgt.model.form;

import eeet2582.realestatemgt.model.helper.ObjectHasUserHouse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MeetingForm extends ObjectHasUserHouse {

    private String date;
    private String time;
    private String note;
}
