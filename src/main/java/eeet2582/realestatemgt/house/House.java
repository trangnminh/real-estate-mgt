package eeet2582.realestatemgt.house;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long houseId;

    private String name;
    private Double price;
    private String description;
    private String address;
    private Double longitude;
    private Double latitude;
    private String image;
    private String type;
    private String status;

    public House(String name,
                 Double price,
                 String description,
                 String address,
                 Double longitude,
                 Double latitude,
                 String image,
                 String type,
                 String status) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
        this.type = type;
        this.status = status;
    }
}
