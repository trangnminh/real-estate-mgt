package eeet2582.realestatemgt.model.house;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HouseSearchForm implements Serializable {

    private String city;
    private String district;
    private Double priceFrom;
    private Double priceTo;
    private List<String> statusList;
    private List<String> typeList;
    private String query;
}
