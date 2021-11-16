package eeet2582.realestatemgt.house;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class House {

    private Long houseId;

    private String houseName;
    private String houseDescription;
    private String houseAddress;

    public House(String houseName, String houseDescription, String houseAddress) {
        this.houseName = houseName;
        this.houseDescription = houseDescription;
        this.houseAddress = houseAddress;
    }
}
