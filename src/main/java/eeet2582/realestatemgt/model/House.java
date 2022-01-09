package eeet2582.realestatemgt.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
public class House implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId;

    private String name;
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;
    private Double longitude;
    private Double latitude;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> image;

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
                 List<String> image,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof House house)) return false;
        return Objects.equals(houseId, house.houseId) && Objects.equals(name, house.name) && Objects.equals(price, house.price) && Objects.equals(description, house.description) && Objects.equals(address, house.address) && Objects.equals(longitude, house.longitude) && Objects.equals(latitude, house.latitude) && Objects.equals(image, house.image) && Objects.equals(type, house.type) && Objects.equals(numberOfBeds, house.numberOfBeds) && Objects.equals(squareFeet, house.squareFeet) && Objects.equals(status, house.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseId, name, price, description, address, longitude, latitude, image, type, numberOfBeds, squareFeet, status);
    }
}
