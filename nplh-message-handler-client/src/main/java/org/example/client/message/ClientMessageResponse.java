package org.example.client.message;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ClientMessageResponse {
    public String message;
    public LocalDateTime receiveTime;

    public ClientMessageResponse(String message) {
        this.message = message;
        this.receiveTime = LocalDateTime.now();
    }
}


