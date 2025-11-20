package org.example.domain.server.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServerMessage {
    public String message;
    public LocalDateTime receiveTime;
    public List<String> responses;

    public ServerMessage(String message) {
        this.message = message;
        this.receiveTime = LocalDateTime.now();
        this.responses = new ArrayList<>();
    }

    public ServerMessage(String message, List<String> responses) {
        this.message = message;
        this.receiveTime = LocalDateTime.now();
        this.responses = responses;
    }

    public void addResponse(String response) {
        if (responses == null) {
            responses = new ArrayList<>();
        }
        if (message != null) {
            responses.add(response);
        }
    }
}
