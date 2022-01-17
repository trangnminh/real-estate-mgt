package eeet2582.realestatemgt.model.house;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "house_location", uniqueConstraints = {@UniqueConstraint(columnNames = {"city", "district"})})
public class HouseLocation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String city;
    private String district;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<House> houses;

    public HouseLocation(String city, String district) {
        this.city = city;
        this.district = district;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof HouseLocation other))
            return false;
        return (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
    }
}
