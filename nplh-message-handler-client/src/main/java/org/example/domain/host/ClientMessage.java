package org.example.domain.host;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientMessage {
    public String message;
    public String controlId;
    public List<ClientMessageResponse> responses;

    public ClientMessage(String message, String controlId) {
        this.message = message;
        this.controlId = controlId;
        this.responses = new ArrayList<>();
    }

    public void addResponse(String message) {
        if (responses == null) {
            responses = new ArrayList<>();
        }
        if (message != null) {
            responses.add(new ClientMessageResponse(message));
        }
    }
}
