package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
public class StainOrder extends WSSegment {
    @JacksonXmlProperty(localName = "OrderRequest")
    private String orderRequest;
    @JacksonXmlProperty(localName = "CaseID")
    private String caseID;
    @JacksonXmlProperty(localName = "TechConsultCaseId")
    private String techConsultCaseId;
    @JacksonXmlProperty(localName = "RegisterDateTime")
    private String registerDateTime;
    @JacksonXmlProperty(localName = "FacilityCode")
    private String facilityCode;
    @JacksonXmlProperty(localName = "FacilityName")
    private String facilityName;
    @JacksonXmlProperty(localName = "StainProtocol")
    private StainProtocol stainProtocol;
    @JacksonXmlProperty(localName = "CollectDate")
    private String collectDate;
    @JacksonXmlProperty(localName = "ReceivedDate")
    private String receivedDateTime;
    @JacksonXmlProperty(localName = "TissueType")
    private String tissueType;
    @JacksonXmlProperty(localName = "TissueDescription")
    private String tissueDescription;
    @JacksonXmlProperty(localName = "SurgicalProcedureName")
    private String surgicalProcedureName;
    @JacksonXmlProperty(localName = "SurgicalProcedureDescription")
    private String surgicalProcedureDescription;
    @JacksonXmlProperty(localName = "Pathologist")
    private Pathologist pathologist;
    @JacksonXmlProperty(localName = "Slide")
    private Slide slide;
    @JacksonXmlProperty(localName = "Block")
    private Block block;
    @JacksonXmlProperty(localName = "Specimen")
    private Specimen specimen;
    @JacksonXmlProperty(localName = "LastPrinted")
    private LocalDateTime lastPrinted;

    public static StainOrder fromMessage(String orderRequest, Message message, org.example.domain.message.entity.Slide entitySlide) {
        StainOrder stainOrder = new StainOrder();
        org.example.domain.message.entity.Block entityBlock = entitySlide.getBlockParent(message);
        org.example.domain.message.entity.Specimen entitySpecimen = entityBlock.getSpecimenParent(message);

        stainOrder.setOrderRequest(orderRequest);
        stainOrder.setCaseID(message.getOrder().getSampleId());
        stainOrder.setTechConsultCaseId(message.getOrder().getExtSampleId());
        stainOrder.setRegisterDateTime(convertToXmlDateTime(message.getHeader().getMessageDateTime()));
        stainOrder.setFacilityCode(entitySpecimen.getFacilityCode());
        stainOrder.setFacilityName(entitySpecimen.getFacilityName());
        stainOrder.setStainProtocol(StainProtocol.FromStainProtocol(entitySlide.getStainProtocol()));
        stainOrder.setCollectDate(convertToXmlDateTime(entitySpecimen.getCollectDateTime()));
        stainOrder.setReceivedDateTime(convertToXmlDateTime(entitySpecimen.getReceivedDateTime()));
        stainOrder.setTissueType(entitySpecimen.getProcedure().getTissue().getType());
        stainOrder.setTissueDescription(entitySpecimen.getProcedure().getTissue().getDescription());
        stainOrder.setSurgicalProcedureName(entitySpecimen.getProcedure().getSurgical().getName());
        stainOrder.setSurgicalProcedureDescription(entitySpecimen.getProcedure().getSurgical().getDescription());
        stainOrder.setPathologist(Pathologist.FromPathologist(message.getOrder().getPathologist()));
        stainOrder.setBlock(Block.FromBlock(entityBlock));
        stainOrder.setSlide(Slide.FromSlide(entitySlide));
        stainOrder.setSpecimen(Specimen.FromSpecimen(entitySpecimen));
        //TODO check TIMEZONE
        if (entitySlide.getLabelPrinted() != null && !entitySlide.getLabelPrinted().isEmpty()) {
            stainOrder.setLastPrinted(LocalDateTime.parse(entitySlide.getLabelPrinted()));
        }

        return stainOrder;
    }

    private boolean isEmpty() {
        return (block == null || block.isEmpty())
                || (slide == null || slide.isEmpty())
                ||(specimen == null || specimen.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StainOrder that = (StainOrder) o;

        LocalDate thisDate = (this.lastPrinted == null) ? null : this.lastPrinted.toLocalDate();
        LocalDate thatDate = (that.lastPrinted == null) ? null : that.lastPrinted.toLocalDate();

        return Objects.equals(orderRequest, that.orderRequest)
                && Objects.equals(caseID, that.caseID)
                && Objects.equals(techConsultCaseId, that.techConsultCaseId)
                && Objects.equals(registerDateTime, that.registerDateTime)
                && Objects.equals(facilityCode, that.facilityCode)
                && Objects.equals(facilityName, that.facilityName)
                && Objects.equals(stainProtocol, that.stainProtocol)
                && Objects.equals(collectDate, that.collectDate)
                && Objects.equals(receivedDateTime, that.receivedDateTime)
                && Objects.equals(tissueType, that.tissueType)
                && Objects.equals(tissueDescription, that.tissueDescription)
                && Objects.equals(surgicalProcedureName, that.surgicalProcedureName)
                && Objects.equals(surgicalProcedureDescription, that.surgicalProcedureDescription)
                && Objects.equals(pathologist, that.pathologist)
                && Objects.equals(slide, that.slide)
                && Objects.equals(block, that.block)
                && Objects.equals(specimen, that.specimen)
                && (Objects.equals(thisDate, thatDate)
                    || Objects.equals(thisDate.minusDays(1), thatDate)
                    || Objects.equals(thisDate.plusDays(1), thatDate));
    }

    public String toString(int indentationLevel) {
        String labOrder = addIndentation(indentationLevel) + "<StainOrder>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            labOrder +=  addIndentation(indentationLevel) + "<OrderRequest>" +  nullSafe(orderRequest) + "</OrderRequest>\n"
                    + addIndentation(indentationLevel) + "<CaseID>" +  nullSafe(caseID) + "</CaseID>\n"
                    + addIndentation(indentationLevel) + "<TechConsultCaseId>" +  nullSafe(techConsultCaseId) + "</TechConsultCaseId>\n"
                    + addIndentation(indentationLevel) + "<RegisterDateTime>" +  nullSafe(registerDateTime) + "</RegisterDateTime>\n"
                    + addIndentation(indentationLevel) + "<FacilityCode>" +  nullSafe(facilityCode) + "</FacilityCode>\n"
                    + addIndentation(indentationLevel) + "<FacilityName>" +  nullSafe(facilityName) + "</FacilityName>\n"
                    + nullSafe(stainProtocol, StainProtocol::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<CollectDate>" +  nullSafe(collectDate) + "</CollectDate>\n"
                    + addIndentation(indentationLevel) + "<ReceivedDate>" +  nullSafe(receivedDateTime) + "</ReceivedDate>\n"
                    + addIndentation(indentationLevel) + "<TissueType>" +  nullSafe(tissueType) + "</TissueType>\n"
                    + addIndentation(indentationLevel) + "<TissueDescription>" +  nullSafe(tissueDescription) + "</TissueDescription>\n"
                    + addIndentation(indentationLevel) + "<SurgicalProcedureName>" +  nullSafe(surgicalProcedureName) + "</SurgicalProcedureName>\n"
                    + addIndentation(indentationLevel) + "<SurgicalProcedureDescription>" +  nullSafe(surgicalProcedureDescription) + "</SurgicalProcedureDescription>\n"
                    + nullSafe(pathologist, Pathologist::new).toString(indentationLevel) + "\n"
                    + nullSafe(block, Block::new).toString(indentationLevel) + "\n"
                    + nullSafe(slide, Slide::new).toString(indentationLevel) + "\n"
                    //TODO missing supplemental
                    + nullSafe(specimen, Specimen::new).toString(indentationLevel) + "\n"
                    + nullSafe(block, Block::new).toStringTube(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<LastPrinted>" +  nullSafe(lastPrinted) + "</LastPrinted>\n";

            indentationLevel --;
        }

        labOrder += addIndentation(indentationLevel) + "</StainOrder>";
        return labOrder;
    }
}
