package eeet2582.realestatemgt.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;

public class IdToRentalParser implements JsonDeserializer<Rental> {

    @Autowired
    private final RentalRepository rentalRepository;

    public IdToRentalParser(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental deserialize(JsonElement jsonElement,
                              Type type,
                              JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        try {
            return rentalRepository.getById(jsonElement.getAsLong());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
