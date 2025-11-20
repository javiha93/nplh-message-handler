package org.example.domain.ws.VSS.VSSToNPLH.response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder.VSS_ProcessOrder;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class CommunicationResponse extends WSSegment implements WSMessage {

    @JacksonXmlProperty(localName = "IsSuccessful")
    private Boolean status;
    private String soapAction;
    @JacksonXmlProperty(localName = "Code")
    private String errorCode;
    @JacksonXmlProperty(localName = "ErrorText")
    private String errorText;

    public static CommunicationResponse FromSoapActionOk(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setStatus(true);

        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction, String errorText) {
        CommunicationResponse communicationResponse = FromSoapActionError(soapAction);

        communicationResponse.setErrorCode("9999");
        communicationResponse.setErrorText(errorText);


        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setStatus(false);

        return communicationResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommunicationResponse ack = (CommunicationResponse) o;

        return Objects.equals(status, ack.status)
                && Objects.equals(soapAction, ack.soapAction)
                && Objects.equals(errorCode, ack.errorCode)
                && Objects.equals(errorText, ack.errorText);
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<" + soapAction + "Response>\n" +
                addIndentation(indentationLevel) + "<" + soapAction + "Result>\n" +
                addIndentation(indentationLevel + 1) + "<Code>" + nullSafe(errorCode) + "</Code>\n" +
                addIndentation(indentationLevel + 1) + "<ErrorText>" + nullSafe(errorText) + "</ErrorText>\n" +
                addIndentation(indentationLevel + 1) + "<IsSuccessful>" + status + "</IsSuccessful>\n" +
                addIndentation(indentationLevel) + "</" + soapAction + "Result>\n" +
                "</" + soapAction + "Response>";
    }

    public static CommunicationResponse fromXml(String xml, String soapAction) {
        try {
            // Extraer el bloque relevante si viene dentro de un SOAP
            int start = xml.indexOf("<" + soapAction + "Result");
            int end = xml.indexOf("</" + soapAction + "Result>") + ("</" + soapAction + "Result>").length();
            String processXml = xml.substring(start, end);

            // Configurar mapper XML
            XmlMapper mapper = new XmlMapper();
            mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            CommunicationResponse communicationResponse = mapper.readValue(processXml, CommunicationResponse.class);
            communicationResponse.soapAction = soapAction;
            return communicationResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }
}
