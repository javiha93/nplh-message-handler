package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Specimen extends WSSegment {

    @JacksonXmlProperty(localName = "Barcode")
    private String barcode;
    @JacksonXmlProperty(localName = "FixativeTime")
    private String fixativeTime;
    @JacksonXmlProperty(localName = "FixativeType")
    private String fixativeType;
    @JacksonXmlProperty(localName = "Sequence")
    private String sequence;
    @JacksonXmlProperty(localName = "SurgicalProcedureDescription")
    private String surgicalProcedureDescription;
    @JacksonXmlProperty(localName = "SurgicalProcedureName")
    private String surgicalProcedureName;
    @JacksonXmlProperty(localName = "TechConsultId")
    private String techConsultId;
    @JacksonXmlProperty(localName = "TissueDescription")
    private String tissueDescription;
    @JacksonXmlProperty(localName = "TissueDimensionHeight")
    private String tissueDimensionHeight;
    @JacksonXmlProperty(localName = "TissueDimensionLength")
    private String tissueDimensionLength;
    @JacksonXmlProperty(localName = "TissueDimensionWeight")
    private String tissueDimensionWeight;
    @JacksonXmlProperty(localName = "TissueDimensionWidth")
    private String tissueDimensionWidth;
    @JacksonXmlProperty(localName = "TissueName")
    private String tissueName;
    @JacksonXmlProperty(localName = "FacilityCode")
    private String facilityCode;
    @JacksonXmlProperty(localName = "FacilityName")
    private String facilityName;
    @JacksonXmlProperty(localName = "ObservationDateTime")
    private String observationDateTime;

    public static Specimen Default(org.example.domain.message.entity.Specimen entitySpecimen) {
        Specimen specimen = new Specimen();

        specimen.barcode = entitySpecimen.getId();
        specimen.sequence = entitySpecimen.getSequence();
        specimen.surgicalProcedureDescription = entitySpecimen.getProcedure().getSurgical().getDescription();
        specimen.surgicalProcedureName = entitySpecimen.getProcedure().getSurgical().getName();
        specimen.techConsultId = entitySpecimen.getExternalId();
        specimen.tissueDescription = entitySpecimen.getProcedure().getTissue().getDescription();
        specimen.tissueName = entitySpecimen.getProcedure().getTissue().getType();
        specimen.facilityCode = entitySpecimen.getFacilityCode();
        specimen.facilityName = entitySpecimen.getFacilityName();
        specimen.observationDateTime = "2014-10-14T00:00:00"; // convertToXmlDateTime(entitySpecimen.getCollectDateTime());

        return specimen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specimen specimen = (Specimen) o;

        return Objects.equals(barcode, specimen.barcode)
                && Objects.equals(fixativeTime, specimen.fixativeTime)
                && Objects.equals(fixativeType, specimen.fixativeType)
                && Objects.equals(sequence, specimen.sequence)
                && Objects.equals(surgicalProcedureDescription, specimen.surgicalProcedureDescription)
                && Objects.equals(surgicalProcedureName, specimen.surgicalProcedureName)
                && Objects.equals(tissueDescription, specimen.tissueDescription)
                && Objects.equals(tissueDimensionHeight, specimen.tissueDimensionHeight)
                && Objects.equals(tissueDimensionLength, specimen.tissueDimensionLength)
                && Objects.equals(tissueDimensionWeight, specimen.tissueDimensionWeight)
                && Objects.equals(tissueDimensionWidth, specimen.tissueDimensionWidth)
                && Objects.equals(tissueName, specimen.tissueName)
                && Objects.equals(facilityCode, specimen.facilityCode)
                && Objects.equals(facilityName, specimen.facilityName)
                && Objects.equals(observationDateTime, specimen.observationDateTime);
    }

    public boolean isEmpty() {
        return Stream.of(
                        barcode,
                        fixativeTime,
                        fixativeType,
                        sequence,
                        surgicalProcedureDescription,
                        surgicalProcedureName,
                        techConsultId,
                        tissueDescription,
                        tissueDimensionHeight,
                        tissueDimensionLength,
                        tissueDimensionWeight,
                        tissueDimensionWidth,
                        tissueName,
                        facilityCode,
                        facilityName,
                        observationDateTime
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Specimen>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<FixativeTime>" +  nullSafe(fixativeTime) + "</FixativeTime>\n"
                    + addIndentation(indentationLevel) + "<FixativeType>" +  nullSafe(fixativeType) + "</FixativeType>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<SurgicalProcedureDescription>" +  nullSafe(surgicalProcedureDescription) + "</SurgicalProcedureDescription>\n"
                    + addIndentation(indentationLevel) + "<SurgicalProcedureName>" +  nullSafe(surgicalProcedureName) + "</SurgicalProcedureName>\n"
                    + addIndentation(indentationLevel) + "<TechConsultId>" +  nullSafe(techConsultId) + "</TechConsultId>\n"
                    + addIndentation(indentationLevel) + "<TissueDescription>" +  nullSafe(tissueDescription) + "</TissueDescription>\n"
                    + addIndentation(indentationLevel) + "<TissueDimensionHeight>" +  nullSafe(tissueDimensionHeight) + "</TissueDimensionHeight>\n"
                    + addIndentation(indentationLevel) + "<TissueDimensionLength>" +  nullSafe(tissueDimensionLength) + "</TissueDimensionLength>\n"
                    + addIndentation(indentationLevel) + "<TissueDimensionWeight>" +  nullSafe(tissueDimensionWeight) + "</TissueDimensionWeight>\n"
                    + addIndentation(indentationLevel) + "<TissueDimensionWidth>" + nullSafe(tissueDimensionWidth) + "</TissueDimensionWidth>\n"
                    + addIndentation(indentationLevel) + "<TissueName>" + nullSafe(tissueName) + "</TissueName>\n"
                    + addIndentation(indentationLevel) + "<FacilityCode>" + nullSafe(facilityCode) + "</FacilityCode>\n"
                    + addIndentation(indentationLevel) + "<FacilityName>" + nullSafe(facilityName) + "</FacilityName>\n"
                    + addIndentation(indentationLevel) + "<ObservationDateTime>" + nullSafe(observationDateTime) + "</ObservationDateTime>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Specimen>";
        return specimen;
    }
}
