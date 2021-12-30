package eeet2582.realestatemgt.model;

import lombok.Value;

@Value
public class ErrorMessage {

  private String message;

  public static ErrorMessage from(final String message) {
    return new ErrorMessage(message);
  }
}
