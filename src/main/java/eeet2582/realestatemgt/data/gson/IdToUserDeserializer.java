package eeet2582.realestatemgt.data.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.service.UserHouseLocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

@RequiredArgsConstructor
public class IdToUserDeserializer implements JsonDeserializer<AppUser> {

    @Autowired
    private final UserHouseLocationUtil userHouseLocationUtil;

    @Override
    public AppUser deserialize(JsonElement jsonElement,
                               Type type,
                               JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        try {
            return userHouseLocationUtil.getUserById(jsonElement.getAsLong());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
