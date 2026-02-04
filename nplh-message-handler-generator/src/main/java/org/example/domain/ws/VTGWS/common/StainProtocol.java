package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class StainProtocol extends WSSegment {
    @JacksonXmlProperty(localName = "ProtocolName")
    String protocolName;
    @JacksonXmlProperty(localName = "ProtocolDescription")
    String protocolDescription;
    @JacksonXmlProperty(localName = "ProtocolNumber")
    String protocolNumber;


    public static StainProtocol Default(org.example.domain.message.entity.StainProtocol entityStainProtocol) {
        StainProtocol stainProtocol = new StainProtocol();

        stainProtocol.protocolName = entityStainProtocol.getName();
        stainProtocol.protocolDescription = entityStainProtocol.getDescription();
        stainProtocol.protocolNumber = entityStainProtocol.getNumber();

        return stainProtocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StainProtocol stainProtocol = (StainProtocol) o;

        return Objects.equals(protocolName, stainProtocol.protocolName)
                && Objects.equals(protocolNumber, stainProtocol.protocolNumber)
                && Objects.equals(protocolDescription, stainProtocol.protocolDescription);
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
                    + addIndentation(indentationLevel) + "<ProtocolName>" +  nullSafe(protocolName) + "</ProtocolName>\n"
                    + addIndentation(indentationLevel) + "<ProtocolNumber>" +  nullSafe(protocolNumber) + "</ProtocolNumber>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</StainProtocol>";
        return specimen;
    }

}
