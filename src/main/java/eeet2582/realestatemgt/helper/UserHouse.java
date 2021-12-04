package eeet2582.realestatemgt.helper;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UserHouse implements Serializable {

    private Long userId;
    private Long houseId;
}
