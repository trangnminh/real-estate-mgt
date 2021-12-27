package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public Message getPublicMessage() {
        final var text = "The API doesn't require an access token to share this message.";

        return Message.from(text);
    }

    public Message getProtectedMessage() {
        final var text = "The API successfully validated your access token.";

        return Message.from(text);
    }

    public Message getAdminMessage() {
        final var text = "The API successfully recognized you as an admin.";

        return Message.from(text);
    }
}
