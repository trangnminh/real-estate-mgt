package eeet2582.realestatemgt.model.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class HouseForm implements Serializable {

    private String name;
    private Double price;
    private String description;
    private String address;
    private Double longitude;
    private Double latitude;
    private List<String> image;
    private String type;
    private Integer numberOfBeds;
    private Integer squareFeet;
    private String status;
    private String city;
    private String district;
}
