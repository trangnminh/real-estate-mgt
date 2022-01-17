package eeet2582.realestatemgt.model.helper;

import lombok.Value;

@Value
public class ErrorMessage {

    String message;

    public static ErrorMessage from(final String message) {
        return new ErrorMessage(message);
    }
}
