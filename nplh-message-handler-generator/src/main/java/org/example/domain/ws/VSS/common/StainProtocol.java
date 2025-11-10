package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class StainProtocol extends WSSegment {
    @JacksonXmlProperty(localName = "ProtocolNumber")
    private String number;
    @JacksonXmlProperty(localName = "ProtocolName")
    private String name;
    @JacksonXmlProperty(localName = "ProtocolProcedure")
    private String procedure;
    @JacksonXmlProperty(localName = "ProtocolType")
    private String type;
    @JacksonXmlProperty(localName = "LastModified")
    private String lastModified;

    public static StainProtocol FromStainProtocol(org.example.domain.message.entity.StainProtocol entityStainProtocol) {
        StainProtocol stainProtocol = new StainProtocol();

        stainProtocol.number = entityStainProtocol.getNumber();
        stainProtocol.name = entityStainProtocol.getName();
        stainProtocol.procedure = entityStainProtocol.getDescription();
        stainProtocol.type = "STAIN";

        return stainProtocol;
    }

    public boolean isEmpty() {
        return Stream.of(number, name, procedure)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StainProtocol stainProtocol = (StainProtocol) o;

        return Objects.equals(number, stainProtocol.number)
                && Objects.equals(name, stainProtocol.name)
                && Objects.equals(procedure, stainProtocol.procedure)
                && Objects.equals(type, stainProtocol.type);
    }

    public String toString(int indentationLevel, String name) {
        String specimen = addIndentation(indentationLevel) + "<" + name + ">\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<ProtocolNumber>" +  nullSafe(number) + "</ProtocolNumber>\n"
                    + addIndentation(indentationLevel) + "<ProtocolName>" +  nullSafe(name) + "</ProtocolName>\n"
                    + addIndentation(indentationLevel) + "<ProtocolProcedure>" +  nullSafe(procedure) + "</ProtocolProcedure>\n"
                    + addIndentation(indentationLevel) + "<ProtocolType>" +  nullSafe(type) + "</ProtocolType>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</" + name + ">";
        return specimen;
    }

    public String toString(int indentationLevel) {
        return toString(indentationLevel, "StainProtocol");
    }
}
