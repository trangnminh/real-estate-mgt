package eeet2582.realestatemgt.service;


import eeet2582.realestatemgt.model.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  public Message getPublicMessage() {
    final var text = "Guest";
    return Message.from(text);
  }

  public Message getProtectedMessage() {
    final var text = "Login as user";
    return Message.from(text);
  }

  public Message getAdminMessage() {
    final var text = "Admin login sucessfully";
    return Message.from(text);
  }
}
