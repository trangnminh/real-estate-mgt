package eeet2582.realestatemgt.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringToTimeParser implements JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(jsonElement.getAsString(), dtf);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
