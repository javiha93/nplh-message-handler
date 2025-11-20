package org.example.domain.client.message;

import lombok.Data;
import org.example.domain.hl7.HL7Message;
import java.time.LocalDateTime;

@Data
public class HL7ResponseMessage {
    HL7Message message;
    String roughMessage;
    LocalDateTime sentTime;
}
