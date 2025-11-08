package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.StringReader;
import java.util.Objects;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "ProcessNewOrder")
public class VTGWS_ProcessNewOrder extends WSSegment implements WSMessage {
    @JacksonXmlProperty(localName = "transactionId")
    private String transactionId;

    @JacksonXmlProperty(localName = "labOrder")
    private LabOrder labOrder;

    public static VTGWS_ProcessNewOrder FromMessage(Message message) {
        VTGWS_ProcessNewOrder processNewOrder = new VTGWS_ProcessNewOrder();

        processNewOrder.setTransactionId(message.getHeader().getMessageControlId());
        processNewOrder.setLabOrder(LabOrder.FromMessage(message));

        return processNewOrder;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessNewOrder>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessNewOrder>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VTGWS_ProcessNewOrder processNewOrder = (VTGWS_ProcessNewOrder) o;
        return Objects.equals(labOrder, processNewOrder.labOrder);
    }

    public static VTGWS_ProcessNewOrder fromXml(String xml) {
        try {
            // Extraer el bloque relevante si viene dentro de un SOAP
            int start = xml.indexOf("<ProcessNewOrder");
            int end = xml.indexOf("</ProcessNewOrder>") + "</ProcessNewOrder>".length();
            String processXml = xml.substring(start, end);

            if (!processXml.contains("xmlns:xsi")) {
                processXml = processXml.replaceFirst(
                        "<ProcessNewOrder",
                        "<ProcessNewOrder xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                );
            }

            // Configurar mapper XML
            XmlMapper mapper = new XmlMapper();
            mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.readValue(processXml, VTGWS_ProcessNewOrder.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }

}
