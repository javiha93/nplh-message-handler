package org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessStainingStatusUpdate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.StainingInfo;
import org.example.domain.ws.VTGWS.common.Slide;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "stainingStatus")
public class VTGWS_ProcessStainingStatusUpdate extends WSSegment implements WSMessage {

    @JacksonXmlProperty(localName = "OrderStatus")
    private String orderStatus;
    @JacksonXmlProperty(localName = "OriginatingHost")
    private String originatingHost;
    @JacksonXmlProperty(localName = "OriginationDate")
    private LocalDate originationDate;
    @JacksonXmlProperty(localName = "ShortName")
    private String shortName;
    @JacksonXmlProperty(localName = "Slide")
    private Slide slide;
    @JacksonXmlProperty(localName = "StainerFriendlyName")
    private String stainerFriendlyName;
    @JacksonXmlProperty(localName = "StainerSerialNumber")
    private String stainerSerialNumber;
    @JacksonXmlProperty(localName = "StainerType")
    private String stainerType;
    @JacksonXmlProperty(localName = "StainingBatchID")
    private String stainingBatchID;
    @JacksonXmlProperty(localName = "StainingHostID")
    private String stainingHostID;
    @JacksonXmlProperty(localName = "StainingRunEstimatedMins")
    private String stainingRunEstimatedMins;
    @JacksonXmlProperty(localName = "StainingRunMins")
    private String stainingRunMins;
    @JacksonXmlProperty(localName = "StainingRunNumber")
    private String stainingRunNumber;
    @JacksonXmlProperty(localName = "StainingRunSlidePosition")
    private String stainingRunSlidePosition;

    public static VTGWS_ProcessStainingStatusUpdate fromSlide(Message message, org.example.domain.message.entity.Slide slide, String status) {
        VTGWS_ProcessStainingStatusUpdate stainingStatusUpdate = new VTGWS_ProcessStainingStatusUpdate();

        stainingStatusUpdate.setOrderStatus(status);
        stainingStatusUpdate.setOriginatingHost("VENTANA Connect");
        stainingStatusUpdate.setOriginationDate(LocalDate.now());
        stainingStatusUpdate.setSlide(Slide.Default(slide));

        StainingInfo stainingInfo = slide.getStainingInfo();
        stainingStatusUpdate.setShortName(stainingInfo.shortName);
        stainingStatusUpdate.setStainerFriendlyName(stainingInfo.stainerFriendlyName);
        stainingStatusUpdate.setStainerSerialNumber(stainingInfo.stainerSerialNumber);
        stainingStatusUpdate.setStainerType(stainingInfo.stainerEffectiveType);
        stainingStatusUpdate.setStainingBatchID(stainingInfo.stainingBatchID);
        stainingStatusUpdate.setStainingHostID(stainingInfo.hostID);
        stainingStatusUpdate.setStainingRunEstimatedMins(stainingInfo.runEstimatedTime);
        stainingStatusUpdate.setStainingRunNumber(stainingInfo.runNumber);
        stainingStatusUpdate.setStainingRunSlidePosition(stainingInfo.slidePosition);
        stainingStatusUpdate.setStainingRunMins(stainingInfo.runTime);

        return stainingStatusUpdate;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return "<StainingStatus>\n" +
                addIndentation(indentationLevel) + "<OrderStatus>" + orderStatus + "</OrderStatus>\n" +
                addIndentation(indentationLevel) + "<OriginatingHost>" + originatingHost + "</OriginatingHost>\n" +
                addIndentation(indentationLevel) + "<OriginationDate>" + originationDate + "</OriginationDate>\n" +
                addIndentation(indentationLevel) + "<ShortName>" + shortName + "</ShortName>\n" +
                slide.toString(indentationLevel) + "\n" +
                addIndentation(indentationLevel) + "<StainerFriendlyName>" + orderStatus + "</StainerFriendlyName>\n" +
                addIndentation(indentationLevel) + "<StainerSerialNumber>" + originatingHost + "</StainerSerialNumber>\n" +
                addIndentation(indentationLevel) + "<StainerType>" + originationDate + "</StainerType>\n" +
                addIndentation(indentationLevel) + "<StainingBatchID>" + shortName + "</StainingBatchID>\n" +
                addIndentation(indentationLevel) + "<StainingHostID>" + orderStatus + "</StainingHostID>\n" +
                addIndentation(indentationLevel) + "<StainingHostVersion>" + originatingHost + "</StainingHostVersion>\n" +
                addIndentation(indentationLevel) + "<StainingRunEstimatedMins>" + originationDate + "</StainingRunEstimatedMins>\n" +
                addIndentation(indentationLevel) + "<StainingRunMins>" + shortName + "</StainingRunMins>\n" +
                addIndentation(indentationLevel) + "<StainingRunNumber>" + originationDate + "</StainingRunNumber>\n" +
                addIndentation(indentationLevel) + "<StainingRunSlidePosition>" + shortName + "</StainingRunSlidePosition>\n" +
                "</StainingStatus>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VTGWS_ProcessStainingStatusUpdate that = (VTGWS_ProcessStainingStatusUpdate) o;

        return Objects.equals(orderStatus, that.orderStatus) &&
                Objects.equals(originatingHost, that.originatingHost) &&
                (Objects.equals(originationDate, that.originationDate) ||
                        Objects.equals(originationDate.plusDays(1), that.originationDate) ||
                        Objects.equals(originationDate.minusDays(1), that.originationDate)) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(slide, that.slide) &&
                Objects.equals(stainerFriendlyName, that.stainerFriendlyName) &&
                Objects.equals(stainerSerialNumber, that.stainerSerialNumber) &&
                Objects.equals(stainerType, that.stainerType) &&
                Objects.equals(stainingBatchID, that.stainingBatchID) &&
                Objects.equals(stainingHostID, that.stainingHostID) &&
                Objects.equals(stainingRunEstimatedMins, that.stainingRunEstimatedMins) &&
                Objects.equals(stainingRunMins, that.stainingRunMins) &&
                Objects.equals(stainingRunNumber, that.stainingRunNumber) &&
                Objects.equals(stainingRunSlidePosition, that.stainingRunSlidePosition);
    }

    public static VTGWS_ProcessStainingStatusUpdate fromXml(String xml) {
        try {
            int start = xml.indexOf("<s01:stainingStatus");
            int end = xml.indexOf("</s01:stainingStatus>") + "</s01:stainingStatus>".length();
            String processXml = xml.substring(start, end);

            if (!processXml.contains("xmlns:xsi")) {
                processXml = processXml.replaceFirst(
                        "<s01:stainingStatus",
                        "<s01:stainingStatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                );
            }

            // Configurar mapper XML
            XmlMapper mapper = new XmlMapper();
            mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
            mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return mapper.readValue(processXml, VTGWS_ProcessStainingStatusUpdate.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);

        }
    }
}
