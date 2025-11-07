package org.example.domain.ws.VSS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class StainProtocol extends WSSegment {
    private String number;
    private String name;
    private String procedure;
    private String type;
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
