package eeet2582.realestatemgt.model.house;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HouseSearchForm implements Serializable {

    private String city;
    private String district;
    private Double priceFrom;
    private Double priceTo;
    private List<String> statusList;
    private List<String> typeList;
    private String query;
}
