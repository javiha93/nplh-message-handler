package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Specimen;
import org.example.domain.message.entity.SupplementalInfo;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Slide extends WSSegment {
    @JacksonXmlProperty(localName = "accessionNumber")
    private String accessionNumber;
    @JacksonXmlProperty(localName = "blockBarcode")
    private String blockBarcode;
    @JacksonXmlProperty(localName = "blockNumber")
    private String blockNumber;
    @JacksonXmlProperty(localName = "cloneType")
    private String cloneType;
    @JacksonXmlProperty(localName = "comments")
    private String comments;
    @JacksonXmlProperty(localName = "controlType")
    private String controlType;
    @JacksonXmlProperty(localName = "externalBlockId")
    private String externalBlockId;
    @JacksonXmlProperty(localName = "externalSlideId")
    private String externalSlideId;
    @JacksonXmlProperty(localName = "labelId")
    private String labelId;
    @JacksonXmlProperty(localName = "scoringType")
    private String scoringType;
    @JacksonXmlProperty(localName = "slideNumber")
    private String slideNumber;
    @JacksonXmlProperty(localName = "specimenNumber")
    private String specimenNumber;
    @JacksonXmlProperty(localName = "tissueTest")
    private String tissueType;
    @JacksonXmlProperty(localName = "tissueTestDesc")
    private String tissueTypeDesc;
    @JacksonXmlProperty(localName = "vendor")
    private String vendor;

    public static Slide fromSlide(Message message, org.example.domain.message.entity.Slide entitySlide) {
        Slide slide = new Slide();

        slide.accessionNumber = message.getOrder().getSampleId();
        Block entityBlock = entitySlide.getBlockParent(message);

        slide.blockBarcode = entityBlock.getId();
        slide.blockNumber = entityBlock.getSequence();
        slide.externalBlockId = entityBlock.getExternalId();

        Specimen entitySpecimen = entityBlock.getSpecimenParent(message);
        slide.specimenNumber = entitySpecimen.getSequence();

        slide.cloneType = entitySlide.getControl().getClone();
        slide.comments = entitySlide.getComment();
        slide.controlType = entitySlide.getControl().getName();
        slide.externalSlideId = entitySlide.getExternalId();
        slide.labelId = entitySlide.getId();
        slide.scoringType = entitySlide.getControl().getScoring();
        slide.slideNumber = entitySlide.getSequence();
        slide.tissueType = entitySlide.getStainProtocol().getName();
        slide.tissueTypeDesc = entitySlide.getStainProtocol().getDescription();

        return slide;
    }

    public String toString(int indentationLevel) {

        String slide = addIndentation(indentationLevel) + "<allSlides>\n";

        indentationLevel ++;

        slide += addIndentation(indentationLevel) + "<accessionNumber>" +  nullSafe(accessionNumber) + "</accessionNumber>\n"
                + addIndentation(indentationLevel) + "<blockBarcode>" +  nullSafe(blockBarcode) + "</blockBarcode>\n"
                + addIndentation(indentationLevel) + "<blockNumber>" +  nullSafe(blockNumber) + "</blockNumber>\n"
                + addIndentation(indentationLevel) + "<cloneType>" +  nullSafe(cloneType) + "</cloneType>\n"
                + addIndentation(indentationLevel) + "<comments>" +  nullSafe(comments) + "</comments>\n"
                + addIndentation(indentationLevel) + "<controlType>" +  nullSafe(controlType) + "</controlType>\n"
                + addIndentation(indentationLevel) + "<externalBlockId>" +  nullSafe(externalBlockId) + "</externalBlockId>\n"
                + addIndentation(indentationLevel) + "<externalSlideId>" +  nullSafe(externalSlideId) + "</externalSlideId>\n"
                + addIndentation(indentationLevel) + "<labelId>" +  nullSafe(labelId) + "</labelId>\n"
                + addIndentation(indentationLevel) + "<scoringType>" +  nullSafe(scoringType) + "</scoringType>\n"
                + addIndentation(indentationLevel) + "<slideNumber>" +  nullSafe(slideNumber) + "</slideNumber>\n"
                + addIndentation(indentationLevel) + "<specimenNumber>" +  nullSafe(specimenNumber) + "</specimenNumber>\n"
                + addIndentation(indentationLevel) + "<tissueType>" +  nullSafe(tissueType) + "</tissueType>\n"
                + addIndentation(indentationLevel) + "<tissueTypeDesc>" +  nullSafe(tissueTypeDesc) + "</tissueTypeDesc>\n"
                + addIndentation(indentationLevel) + "<vendor>" +  nullSafe(vendor) + "</vendor>\n";

        indentationLevel --;

        slide += addIndentation(indentationLevel) + "</allSlides>\n";

        return slide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Slide slide = (Slide) o;

        return Objects.equals(accessionNumber, slide.accessionNumber)
                && Objects.equals(blockBarcode, slide.blockBarcode)
                && Objects.equals(blockNumber, slide.blockNumber)
                && Objects.equals(cloneType, slide.cloneType)
                && Objects.equals(comments, slide.comments)
                && Objects.equals(controlType, slide.controlType)
                && Objects.equals(externalBlockId, slide.externalBlockId)
                && Objects.equals(externalSlideId, slide.externalSlideId)
                && Objects.equals(labelId, slide.labelId)
                && Objects.equals(scoringType, slide.scoringType)
                && Objects.equals(slideNumber, slide.slideNumber)
                && Objects.equals(specimenNumber, slide.specimenNumber)
                && Objects.equals(tissueType, slide.tissueType)
                && Objects.equals(tissueTypeDesc, slide.tissueTypeDesc)
                && Objects.equals(vendor, slide.vendor);
    }

}
