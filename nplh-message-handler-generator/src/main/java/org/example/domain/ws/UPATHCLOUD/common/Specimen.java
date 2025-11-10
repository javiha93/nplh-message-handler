package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.SupplementalInfo;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Specimen extends WSSegment {
    @JacksonXmlProperty(localName = "accessionNumber")
    private String accessionNumber;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "allSlides")
    private List<Slide> allSlides;
    @JacksonXmlProperty(localName = "description")
    private String description;
    @JacksonXmlProperty(localName = "externalSpecimenId")
    private String externalSpecimenId;
    @JacksonXmlProperty(localName = "grossDescription")
    private String grossDescription;
    @JacksonXmlProperty(localName = "specimenBarcode")
    private String specimenBarcode;
    @JacksonXmlProperty(localName = "specimenCollectedDate")
    private String specimenCollectedDate;
    @JacksonXmlProperty(localName = "specimenNumber")
    private String specimenNumber;
    @JacksonXmlProperty(localName = "specimenReceivedDate")
    private String specimenReceivedDate;
    @JacksonXmlProperty(localName = "surgicalProcedure")
    private String surgicalProcedure;
    @JacksonXmlProperty(localName = "tissueType")
    private String tissueType;
    @JacksonXmlProperty(localName = "tissueTypeDesc")
    private String tissueTypeDesc;

    public static Specimen fromSpecimen(Message message, org.example.domain.message.entity.Specimen entitySpecimen) {
        Specimen specimen = new Specimen();

        specimen.accessionNumber = message.getOrder().getSampleId();
        specimen.externalSpecimenId = entitySpecimen.getExternalId();
        specimen.specimenBarcode = entitySpecimen.getId();
        specimen.specimenCollectedDate = convertToXmlDateTime(entitySpecimen.getCollectDateTime());
        specimen.specimenNumber = entitySpecimen.getSequence();
        specimen.specimenReceivedDate = convertToXmlDateTime(entitySpecimen.getReceivedDateTime());
        specimen.surgicalProcedure = entitySpecimen.getProcedure().getSurgical().getName();
        specimen.tissueType = entitySpecimen.getProcedure().getTissue().getType();
        specimen.tissueTypeDesc = entitySpecimen.getProcedure().getTissue().getDescription();


        SupplementalInfoList supplementalInfoList = entitySpecimen.getSupplementalInfos();
        for (SupplementalInfo supplementalInfo : supplementalInfoList.getSupplementalInfoList()) {
            //if (supplementalInfo.get)
            //TODO get only grossDescription
            specimen.grossDescription = supplementalInfo.getValue();
        }

        specimen.allSlides = new ArrayList<>();
        for (org.example.domain.message.entity.Slide slide : entitySpecimen.getAllSlides()) {
            specimen.allSlides.add(Slide.fromSlide(message, slide));
        }

        return specimen;
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<allSpecimens>\n";

        indentationLevel ++;

        specimen += addIndentation(indentationLevel) + "<accessionNumber>" +  nullSafe(accessionNumber) + "</accessionNumber>\n";

        for (Slide slide: allSlides) {
            specimen += nullSafe(slide, Slide::new).toString(indentationLevel);
        }

        specimen += addIndentation(indentationLevel) + "<description>" +  nullSafe(description) + "</description>\n"
                + addIndentation(indentationLevel) + "<externalSpecimenId>" +  nullSafe(externalSpecimenId) + "</externalSpecimenId>\n"
                + addIndentation(indentationLevel) + "<grossDescription>" +  nullSafe(grossDescription) + "</grossDescription>\n"
                + addIndentation(indentationLevel) + "<specimenBarcode>" +  nullSafe(specimenBarcode) + "</specimenBarcode>\n"
                + addIndentation(indentationLevel) + "<specimenCollectedDate>" +  nullSafe(specimenCollectedDate) + "</specimenCollectedDate>\n"
                + addIndentation(indentationLevel) + "<specimenNumber>" +  nullSafe(specimenNumber) + "</specimenNumber>\n"
                + addIndentation(indentationLevel) + "<specimenReceivedDate>" +  nullSafe(specimenReceivedDate) + "</specimenReceivedDate>\n"
                + addIndentation(indentationLevel) + "<surgicalProcedure>" +  nullSafe(surgicalProcedure) + "</surgicalProcedure>\n"
                + addIndentation(indentationLevel) + "<tissueType>" +  nullSafe(tissueType) + "</tissueType>\n"
                + addIndentation(indentationLevel) + "<tissueTypeDesc>" +  nullSafe(tissueTypeDesc) + "</tissueTypeDesc>\n";

        indentationLevel --;

        specimen += addIndentation(indentationLevel) + "</allSpecimens>";

        return specimen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specimen specimen = (Specimen) o;

        return Objects.equals(accessionNumber, specimen.accessionNumber)
                && Objects.equals(allSlides, specimen.allSlides)
                && Objects.equals(description, specimen.description)
                && Objects.equals(externalSpecimenId, specimen.externalSpecimenId)
                && Objects.equals(grossDescription, specimen.grossDescription)
                && Objects.equals(specimenBarcode, specimen.specimenBarcode)
                && Objects.equals(specimenCollectedDate, specimen.specimenCollectedDate)
                && Objects.equals(specimenNumber, specimen.specimenNumber)
                && Objects.equals(specimenReceivedDate, specimen.specimenReceivedDate)
                && Objects.equals(surgicalProcedure, specimen.surgicalProcedure)
                && Objects.equals(tissueType, specimen.tissueType)
                && Objects.equals(tissueTypeDesc, specimen.tissueTypeDesc);
    }


}
