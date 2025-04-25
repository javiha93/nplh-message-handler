package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.VTGWS.WSSegment;

import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class StainProtocol extends WSSegment {
    String protocolName;
    String protocolDescription;
    String protocolNumber;


    public static StainProtocol Default(org.example.domain.message.entity.StainProtocol entityStainProtocol) {
        StainProtocol stainProtocol = new StainProtocol();

        stainProtocol.protocolName = entityStainProtocol.getName();
        stainProtocol.protocolDescription = entityStainProtocol.getDescription();
        stainProtocol.protocolNumber = entityStainProtocol.getNumber();

        return stainProtocol;
    }

    private boolean isEmpty() {
        return Stream.of(protocolName, protocolDescription, protocolNumber)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<StainProtocol>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<ProtocolDescription>" +  nullSafe(protocolDescription) + "</ProtocolDescription>\n"
                    + addIndentation(indentationLevel) + "<ProtocolName>" +  nullSafe(protocolNumber) + "</ProtocolName>\n"
                    + addIndentation(indentationLevel) + "<ProtocolNumber>" +  nullSafe(protocolNumber) + "</ProtocolNumber>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</StainProtocol>";
        return specimen;
    }

}
