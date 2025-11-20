package org.example.domain.server.message;

import lombok.Data;
import org.example.domain.ws.WSMessage;

import java.time.LocalDateTime;

@Data
public class WSResponseMessage {
    WSMessage message;
    String roughMessage;
    LocalDateTime sentTime;
}
