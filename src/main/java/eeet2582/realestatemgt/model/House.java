package eeet2582.realestatemgt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;

    private String name;
    private Double price;
    private String description;
    private String address;
    private Double longitude;
    private Double latitude;
    private String image;
    private String type;
    private Integer numberOfBeds;
    private Integer squareFeet;
    private String status;

    public House(String name,
                 Double price,
                 String description,
                 String address,
                 Double longitude,
                 Double latitude,
                 String image,
                 String type,
                 Integer numberOfBeds,
                 Integer squareFeet,
                 String status) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.type = type;
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.status = status;
    }
}
