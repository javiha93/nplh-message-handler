package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.VTGWS.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Specimen extends WSSegment {

    private String barcode;
    private String fixativeTime;
    private String fixativeType;
    private String sequence;
    private String surgicalProcedureDescription;
    private String surgicalProcedureName;
    private String techConsultId;
    private String tissueDescription;
    private String tissueDimensionHeight;
    private String tissueDimensionLength;
    private String tissueDimensionWeight;
    private String tissueDimensionWidth;
    private String tissueName;
    private String facilityCode;
    private String facilityName;
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
        specimen.observationDateTime = entitySpecimen.getCollectDateTime();

        return specimen;
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
