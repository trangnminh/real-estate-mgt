package eeet2582.realestatemgt.model.helper;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ObjectHasUserHouse implements Serializable {

    private Long userId;
    private Long houseId;
}
