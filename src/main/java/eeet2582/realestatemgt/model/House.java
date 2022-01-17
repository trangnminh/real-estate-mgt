package eeet2582.realestatemgt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eeet2582.realestatemgt.model.helper.HouseLocation;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "location_id")
    private HouseLocation location;

    @OneToMany(orphanRemoval = true, mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Meeting> meetingList;

    @OneToMany(orphanRemoval = true, mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Deposit> depositList;

    @OneToMany(orphanRemoval = true, mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Rental> rentalList;

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
                 String status,
                 HouseLocation location) {
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
        this.location = location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof House other))
            return false;
        return (this.houseId == null && other.houseId == null)
                || (this.houseId != null && this.houseId.equals(other.houseId));
    }
}
