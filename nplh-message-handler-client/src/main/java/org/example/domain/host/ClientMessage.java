package org.example.domain.host;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientMessage {
    public String message;
    public List<String> responses;

    public ClientMessage(String message) {
        this.message = message;
        this.responses = new ArrayList<>();
    }

    public void addResponse(String message) {
        if (responses == null) {
            responses = new ArrayList<>();
        }
        responses.add(message);
    }
}
