package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.WSSegment;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Client extends WSSegment {
    @JacksonXmlProperty(localName = "clientId")
    private String clientId;
    @JacksonXmlProperty(localName = "clientName")
    private String clientName;

    public static Client fromSpecimen(Specimen specimen) {
        Client client = new Client();

        client.clientId = specimen.getFacilityCode();
        client.clientName = specimen.getFacilityName();

        return client;
    }

    public String toString(int indentationLevel) {
        String client = addIndentation(indentationLevel) + "<client>\n";

        indentationLevel ++;

        client +=  addIndentation(indentationLevel) + "<clientId>" +  nullSafe(clientId) + "</clientId>\n"
                + addIndentation(indentationLevel) + "<clientName>" +  nullSafe(clientName) + "</clientName>\n";

        indentationLevel --;

        client += addIndentation(indentationLevel) + "</client>";

        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return Objects.equals(clientId, client.clientId)
                && Objects.equals(clientName, client.clientName);
    }

}
