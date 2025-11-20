package org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.VSS.common.Patient;
import org.example.domain.ws.VSS.common.Physician;
import org.example.domain.ws.VSS.common.StainOrder;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class VSS_ProcessOrder extends WSSegment implements WSMessage {
    @JacksonXmlProperty(localName = "Patient")
    private Patient patient;
    @JacksonXmlProperty(localName = "Physician")
    private Physician physician;
    @JacksonXmlProperty(localName = "CaseID")
    private String caseId;
    @JacksonXmlElementWrapper(localName = "StainOrders")
    @JacksonXmlProperty(localName = "StainOrder")
    private List<StainOrder> stainOrderList;

    public static VSS_ProcessOrder fromMessage(Message message, String orderRequest) {
        VSS_ProcessOrder processOrder = new VSS_ProcessOrder();

        processOrder.setPatient(Patient.fromPatient(message.getPatient()));
        processOrder.setPhysician(Physician.FromPhysician(message.getPhysician()));
        processOrder.setCaseId(message.getOrder().getSampleId());

        List<Slide> allSlides = message.getAllSlides();
        List<StainOrder> stainOrders = new ArrayList<>();
        for (Slide slide: allSlides) {
            stainOrders.add(StainOrder.fromMessage(orderRequest, message, slide));
        }
        processOrder.setStainOrderList(stainOrders);


        return processOrder;
    }

    public static VSS_ProcessOrder fromMessage(Message message, Slide slide, String orderRequest) {
        VSS_ProcessOrder processOrder = new VSS_ProcessOrder();

        processOrder.setPatient(Patient.fromPatient(message.getPatient()));
        processOrder.setPhysician(Physician.FromPhysician(message.getPhysician()));
        processOrder.setCaseId(message.getOrder().getSampleId());

        List<StainOrder> stainOrders = new ArrayList<>();
        stainOrders.add(StainOrder.fromMessage(orderRequest, message, slide));

        processOrder.setStainOrderList(stainOrders);


        return processOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VSS_ProcessOrder processOrder = (VSS_ProcessOrder) o;

        return Objects.equals(caseId, processOrder.caseId)
                && Objects.equals(patient, processOrder.patient)
                && Objects.equals(physician, processOrder.physician)
                && Objects.equals(stainOrderList, processOrder.stainOrderList);
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        String processOrder = "<ProcessOrder>\n" + addIndentation(indentationLevel) + "<OrderCase>";

        indentationLevel ++;

        processOrder += patient.toString(indentationLevel) +"\n"
                + physician.toString(indentationLevel) + "\n"
                + addIndentation(indentationLevel) + "<CaseID>" + caseId + "</CaseID>\n"
                + addIndentation(indentationLevel) + "<StainOrders>" + "\n";

        indentationLevel ++;
        if (stainOrderList != null && !stainOrderList.isEmpty()) {

            for (StainOrder stainOrder: stainOrderList) {
                processOrder += nullSafe(stainOrder, StainOrder::new).toString(indentationLevel) + "\n";
            }
        }
        indentationLevel --;

        processOrder += addIndentation(indentationLevel) + "</StainOrders>" + "\n";

        indentationLevel --;

        processOrder += addIndentation(indentationLevel) + "</OrderCase>" + "\n"
                + "</ProcessOrder>";

        return processOrder;
    }

    public static VSS_ProcessOrder fromXml(String xml) {
        try {
            // Extraer el bloque relevante si viene dentro de un SOAP
            int start = xml.indexOf("<OrderCase");
            int end = xml.indexOf("</OrderCase>") + "</OrderCase>".length();
            String processXml = xml.substring(start, end);

            // Configurar mapper XML
            XmlMapper mapper = new XmlMapper();
            mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return mapper.readValue(processXml, VSS_ProcessOrder.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }
}
