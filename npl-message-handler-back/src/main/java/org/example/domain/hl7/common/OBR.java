
package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.*;
import org.example.domain.message.entity.supplementalInfo.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OBR extends HL7Segment {

    @HL7Position(position = 1)
    private String segmentNumber;

    @HL7Position(position = 2)
    private String externalSampleId;

    @HL7Position(position = 2)
    private String sampleId;

    @HL7Position(position = 4, subPosition = 1)
    private String protocolNumber;

    @HL7Position(position = 4, subPosition = 2)
    private String protocolName;

    @HL7Position(position = 4, subPosition = 3)
    private String protocolIdentifier;

    @HL7Position(position = 4, subPosition = 5)
    private String protocolDescription;

    @HL7Position(position = 7, subPosition = 1)
    private String collectDateTime;

    @HL7Position(position = 14)
    private String receivedDateTime;

    @HL7Position(position = 15, subPosition = 1)
    private String tissueType;

    @HL7Position(position = 15, subPosition = 2)
    private String tissueDescription;

    @HL7Position(position = 15, subPosition = 3)
    private String surgicalName;

    @HL7Position(position = 15, subPosition = 4)
    private String surgicalDescription;

    @HL7Position(position = 15, subPosition = 5)
    private String anatomicSite;

    @HL7Position(position = 15, subPosition = 6)
    private String anatomicDescription;

    @HL7Position(position = 15, subPosition = 7)
    private String tissueSubtype;

    @HL7Position(position = 15, subPosition = 8)
    private String tissueSubtypeDesc;

    @HL7Position(position = 16, subPosition = 1)
    private String pathologist;

    @HL7Position(position = 16, subPosition = 2)
    private String pathologistLN;

    @HL7Position(position = 16, subPosition = 3)
    private String pathologistFN;

    @HL7Position(position = 16, subPosition = 4)
    private String pathologistMN;

    @HL7Position(position = 16, subPosition = 5)
    private String pathologistSuffix;

    @HL7Position(position = 16, subPosition = 6)
    private String pathologistPrefix;

    @HL7Position(position = 16, subPosition = 7)
    private String pathologistAddress1;

    @HL7Position(position = 16, subPosition = 9)
    private String pathologistCity;

    @HL7Position(position = 16, subPosition = 10)
    private String pathologistCountry;

    @HL7Position(position = 16, subPosition = 11)
    private String pathologistState;

    @HL7Position(position = 16, subPosition = 12)
    private String pathologistHomeTel;

    @HL7Position(position = 16, subPosition = 13)
    private String pathologistMobileTel;

    @HL7Position(position = 16, subPosition = 15)
    private String pathologistZip;

    @HL7Position(position = 17, subPosition = 1)
    private String pathologistWorkTel;

    @HL7Position(position = 17, subPosition = 4)
    private String pathologistMail;

    @HL7Position(position = 18)
    private String workFlow;

    @HL7Position(position = 19, subPosition = 1)
    private String slideID;

    @HL7Position(position = 19, subPosition = 2)
    private String sequence;

    @HL7Position(position = 19, subPosition = 3)
    private String extSlideID;

    @HL7Position(position = 19, subPosition = 4)
    private String textComment;

    @HL7Position(position = 20, subPosition = 1)
    private String blockID;

    @HL7Position(position = 20, subPosition = 2)
    private String sequenceBlock;

    @HL7Position(position = 20, subPosition = 3)
    private String extBlockID;

    @HL7Position(position = 20, subPosition = 4)
    private String parentBlockID;

    @HL7Position(position = 21, subPosition = 1)
    private String specimenID;

    @HL7Position(position = 21, subPosition = 2)
    private String sequenceSpecimen;

    @HL7Position(position = 21, subPosition = 3)
    private String extSpecimenID;

    @HL7Position(position = 27, subPosition = 6)
    private String stat;

    @HL7Position(position = 27, subPosition = 8)
    private String caseTags;

    @HL7Position(position = 34, subPosition = 1)
    private String technician;

    @HL7Position(position = 34, subPosition = 2)
    private String technicianLN;

    @HL7Position(position = 34, subPosition = 3)
    private String technicianFN;

    @HL7Position(position = 34, subPosition = 4)
    private String technicianMN;

    private List<SupplementalInfo> supplementalInfos = new ArrayList<>();

    @HL7Position(position = 47)
    private String specialInstruction;

    @HL7Position(position = 47)
    private String specialInstructionArtifact;

    @HL7Position(position = 47)
    private String qualityIssue;

    @HL7Position(position = 47)
    private String qualityIssueArtifact;

    @HL7Position(position = 47)
    private String qualityIssueResolution;

    @HL7Position(position = 47)
    private String tissuePieces;

    @HL7Position(position = 47)
    private String tissuePiecesArtifact;

    @HL7Position(position = 47)
    private String recut;

    @HL7Position(position = 47)
    private String recutArtifact;

    @HL7Position(position = 47)
    private String grossDescription;

    @HL7Position(position = 47)
    private String grossDescriptionArtifact;


    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        OBR obr = new OBR();

        obr.segmentNumber = segmentNumber;
        obr.externalSampleId = sampleId + " " + specimenId + blockId + "-" + slideId;
        obr.protocolNumber = "123";
        obr.protocolName = "H. Advance1";
        obr.protocolIdentifier = "STAIN";
        obr.collectDateTime = "20141014";
        obr.tissueType = "Breast";
        obr.tissueDescription = "Left Breast Upper Node";
        obr.surgicalName = "Breast Biopsy";
        obr.pathologist = "IndiID";
        obr.pathologistLN = "ILastName";
        obr.pathologistFN = "IFirstName";
        obr.pathologistMN = "ImiddleName";
        obr.pathologistSuffix = "Isufix";
        obr.pathologistPrefix = "Iprefix";
        obr.pathologistAddress1 = "Iaddress";
        obr.pathologistCity = "city";
        obr.pathologistCountry = "Icountry";
        obr.pathologistState = "state";
        obr.pathologistHomeTel = "hometel";
        obr.pathologistMobileTel = "mobiletel";
        obr.pathologistWorkTel = "worktel";
        obr.pathologistZip = "zipcode";
        obr.pathologistMail = "Imail@e.com";
        obr.slideID = sampleId + ";" + specimenId + ";" + blockId + ";" + slideId;
        obr.sequence = slideId;
        obr.blockID = sampleId + ";" + specimenId + ";" + blockId;
        obr.sequenceBlock = blockId;
        obr.specimenID = sampleId + ";" + specimenId;
        obr.sequenceSpecimen = specimenId;
        obr.technician = "IndiID";
        obr.technicianLN = "ILastName";
        obr.technicianFN = "IFirstName";
        obr.technicianMN = "ImiddleName";

        obr.supplementalInfos.add(new SpecialInstruction("SPECIALINSTRUCTIONVALUE", "PART"));
        obr.supplementalInfos.add(new QualityIssue("Tissue found", "SLIDE", "Continue processing"));
        obr.supplementalInfos.add(new TissuePieces("6", "BLOCK"));
        obr.supplementalInfos.add(new Recut("RECUTVALUE", "SLIDE"));
        obr.supplementalInfos.add(new GrossDescription("GROSSDESCRIPTIONVALUE", "PART"));

        return obr;
    }

    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber, OBR obr) {
        obr.segmentNumber = segmentNumber;
        obr.externalSampleId = sampleId + " " + specimenId + blockId + "-" + slideId;
        obr.protocolNumber = "123";
        obr.protocolName = "H. Advance1";
        obr.protocolIdentifier = "STAIN";
        obr.collectDateTime = "20141014";
        obr.tissueType = "Breast";
        obr.tissueDescription = "Left Breast Upper Node";
        obr.surgicalName = "Breast Biopsy";
        obr.pathologist = "IndiID";
        obr.pathologistLN = "ILastName";
        obr.pathologistFN = "IFirstName";
        obr.pathologistMN = "ImiddleName";
        obr.pathologistSuffix = "Isufix";
        obr.pathologistPrefix = "Iprefix";
        obr.pathologistAddress1 = "Iaddress";
        obr.pathologistCity = "city";
        obr.pathologistCountry = "Icountry";
        obr.pathologistState = "state";
        obr.pathologistHomeTel = "hometel";
        obr.pathologistMobileTel = "mobiletel";
        obr.pathologistWorkTel = "worktel";
        obr.pathologistZip = "zipcode";
        obr.pathologistMail = "Imail@e.com";
        obr.slideID = sampleId + ";" + specimenId + ";" + blockId + ";" + slideId;
        obr.sequence = slideId;
        obr.blockID = sampleId + ";" + specimenId + ";" + blockId;
        obr.sequenceBlock = blockId;
        obr.specimenID = sampleId + ";" + specimenId;
        obr.sequenceSpecimen = specimenId;
        obr.technician = "IndiID";
        obr.technicianLN = "ILastName";
        obr.technicianFN = "IFirstName";
        obr.technicianMN = "ImiddleName";

        obr.supplementalInfos.add(new SpecialInstruction("SPECIALINSTRUCTIONVALUE", "PART"));
        obr.supplementalInfos.add(new QualityIssue("Tissue found", "SLIDE", "Continue processing"));
        obr.supplementalInfos.add(new TissuePieces("6", "BLOCK"));
        obr.supplementalInfos.add(new Recut("RECUTVALUE", "SLIDE"));
        obr.supplementalInfos.add(new GrossDescription("GROSSDESCRIPTIONVALUE", "PART"));

        return obr;
    }

    public static OBR FromMessage(Slide slide, Message message, OBR obr) {
        Block block = slide.getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        FromMessage(specimen, message, obr);

        obr.externalSampleId = order.getSampleId() + " " + specimen.getSequence() + block.getSequence() + "-" + slide.getSequence();
        obr.protocolNumber = slide.getStainProtocol().getNumber();
        obr.protocolName = slide.getStainProtocol().getName();
        obr.protocolIdentifier = slide.getStainProtocol().getIdentifier();
        obr.slideID = slide.getId();
        obr.sequence = slide.getSequence();
        obr.blockID = block.getId();
        obr.sequenceBlock = block.getSequence();

        if (specimen.getSupplementalInfos() != null) {
            addSupplementalInfos(specimen.getSupplementalInfos().getSupplementalInfoList(), obr.supplementalInfos);
        }
        if (block.getSupplementalInfos() != null) {
            addSupplementalInfos(block.getSupplementalInfos().getSupplementalInfoList(), obr.supplementalInfos);
        }
        if (slide.getSupplementalInfos() != null) {
            addSupplementalInfos(slide.getSupplementalInfos().getSupplementalInfoList(), obr.supplementalInfos);
        }

        return obr;
    }

    private static void addSupplementalInfos(List<SupplementalInfo> infos, List<SupplementalInfo> targetList) {
        for (SupplementalInfo info : infos) {
            // Use the type field to determine the actual supplemental info type
            String type = info.getType();
            if (type == null) continue;
            
            switch (type) {
                case "SPECIALINSTRUCTION":
                    targetList.add(new SpecialInstruction(info.getValue(), info.getArtifact()));
                    break;
                case "QUALITYISSUE":
                    targetList.add(new QualityIssue(info.getValue(), info.getArtifact(), 
                            info.getQualityIssueValue() != null ? info.getQualityIssueValue() : ""));
                    break;
                case "TISSUEPIECES":
                    targetList.add(new TissuePieces(info.getValue(), info.getArtifact()));
                    break;
                case "RECUT":
                    targetList.add(new Recut(info.getValue(), info.getArtifact()));
                    break;
                case "GROSSDESCRIPTION":
                    targetList.add(new GrossDescription(info.getValue(), info.getArtifact()));
                    break;
                default:
                    // For unexpected types, create a generic SupplementalInfo
                    SupplementalInfo newInfo = new SupplementalInfo();
                    newInfo.setType(info.getType());
                    newInfo.setValue(info.getValue());
                    newInfo.setArtifact(info.getArtifact());
                    targetList.add(newInfo);
                    break;
            }
        }
    }

    public static OBR FromMessage(Slide slide, Message message, int segmentNumber, OBR obr) {

        obr = FromMessage(slide, message, obr);
        obr.segmentNumber = String.valueOf(segmentNumber);

        return obr;
    }

    public static OBR FromMessage(Block block, Message message, OBR obr) {
        Specimen specimen = block.getSpecimenParent(message);

        FromMessage(specimen, message, obr);

        obr.blockID = block.getId();
        obr.sequenceBlock = block.getSequence();

        return obr;
    }

    public static OBR FromMessage(Block block, Message message, int segmentNumber, OBR obr) {

        obr = FromMessage(block, message, obr);
        obr.segmentNumber = String.valueOf(segmentNumber);

        return obr;
    }

    public static OBR FromMessage(Specimen specimen, Message message, OBR obr) {
        obr.collectDateTime = specimen.getCollectDateTime();
        obr.tissueType = specimen.getProcedure().getTissue().getType();
        obr.tissueDescription = specimen.getProcedure().getTissue().getDescription();
        obr.surgicalName = specimen.getProcedure().getSurgical().getName();

        Order order = specimen.getOrderParent(message);

        obr = FromMessage(order, obr);

        obr.specimenID = specimen.getId();
        obr.sequenceSpecimen = specimen.getSequence();

        return obr;
    }

    public static OBR FromMessage(Order order, OBR obr) {
        obr.pathologist = order.getPathologist().getCode();
        obr.pathologistLN = order.getPathologist().getLastName();
        obr.pathologistFN = order.getPathologist().getFirstName();
        obr.pathologistMN = order.getPathologist().getMiddleName();
        obr.pathologistSuffix = order.getPathologist().getSuffix();
        obr.pathologistPrefix = order.getPathologist().getPrefix();
        obr.pathologistAddress1 = order.getPathologist().getAddress();
        obr.pathologistCity = order.getPathologist().getCity();
        obr.pathologistCountry = order.getPathologist().getCountry();
        obr.pathologistState = order.getPathologist().getState();
        obr.pathologistHomeTel = order.getPathologist().getHomePhone();
        obr.pathologistMobileTel = order.getPathologist().getMobile();
        obr.pathologistWorkTel = order.getPathologist().getWorkPhone();
        obr.pathologistZip = order.getPathologist().getZip();
        obr.pathologistMail = order.getPathologist().getEmail();

        obr.technician = order.getTechnician().getCode();
        obr.technicianLN = order.getTechnician().getLastName();
        obr.technicianFN = order.getTechnician().getFirstName();
        obr.technicianMN = order.getTechnician().getMiddleName();

        return obr;
    }

    @Override
    public String toString() {
        String value = "OBR|" +
                nullSafe(segmentNumber) + "|" +
                nullSafe(externalSampleId) + "||" +
                nullSafe(protocolNumber) + "^" + nullSafe(protocolName) + "^" + nullSafe(protocolIdentifier) + "^^" + nullSafe(protocolDescription) + "|||" +
                nullSafe(collectDateTime) + "|||||||" +
                nullSafe(receivedDateTime) + "|" +
                nullSafe(tissueType) + "^" + nullSafe(tissueDescription) + "^" + nullSafe(surgicalName) + nullSafe(surgicalDescription) + "^" + nullSafe(anatomicSite) + "^" + nullSafe(anatomicDescription) + "^" + nullSafe(tissueSubtype) + "^" + nullSafe(tissueSubtypeDesc) + "|" +
                nullSafe(pathologist) + "^" + nullSafe(pathologistLN) + "^" + nullSafe(pathologistFN) + "^" + nullSafe(pathologistMN) + "^" + nullSafe(pathologistSuffix) + "^" + nullSafe(pathologistPrefix) + "^" + nullSafe(pathologistAddress1) + "^^" +
                nullSafe(pathologistCity) + "^" + nullSafe(pathologistCountry) + "^" + nullSafe(pathologistState) + "^" + nullSafe(pathologistHomeTel) + "^" + nullSafe(pathologistMobileTel) + "^^" + nullSafe(pathologistZip) + "|" +
                nullSafe(pathologistWorkTel) + "^^^" + nullSafe(pathologistMail) + "|" +
                nullSafe(workFlow) + "|" +
                nullSafe(slideID) + "^" + nullSafe(sequence) + "^" + nullSafe(extSlideID) + "|" +
                nullSafe(blockID) + "^" + nullSafe(sequenceBlock) + "^" + nullSafe(extBlockID) + "|" +
                nullSafe(specimenID) + "^" + nullSafe(sequenceSpecimen) + "^" + nullSafe(extSpecimenID) + "||||||" +
                "^^^^^" + nullSafe(stat) + "^" + nullSafe(caseTags) + "|||||||" +
                nullSafe(technician) + "^" + nullSafe(technicianLN) + "^" + nullSafe(technicianFN) + "^" + nullSafe(technicianMN) + "|||||||||||||";

        return cleanSegment(value + getSupplementalInfo());
    }

    protected String getSupplementalInfo() {
        String supplementalInfo = "";

        for (SupplementalInfo sp : supplementalInfos) {
            if (!supplementalInfo.isEmpty()) {
                supplementalInfo = supplementalInfo + "~";
            }
            if (sp.getClass().equals(QualityIssue.class)) {
                supplementalInfo = supplementalInfo + nullSafe(sp.getType()) + "^" + nullSafe(sp.getValue()) + "^" + nullSafe(sp.getArtifact()) + "^" + nullSafe(((QualityIssue) sp).getOptionalType()) + "^" + nullSafe(((QualityIssue) sp).getOptionalValue());
            } else {
                supplementalInfo = supplementalInfo + nullSafe(sp.getType()) + "^" + nullSafe(sp.getValue()) + "^" + nullSafe(sp.getArtifact()) + "^^";
            }

        }
        return supplementalInfo;
    }

    protected String toStringDeleteCase() {
        String value = "OBR||" +
                nullSafe(sampleId) + "|";          // 2

        return cleanSegment(value);
    }

    protected String toStringDeleteSlide() {
        String value = "OBR||" +
                nullSafe(slideID) + "|";          // 2

        return cleanSegment(value);
    }

    protected String toStringDeleteSpecimen() {
        String value = "OBR||" +
                nullSafe(specimenID) + "|";          // 2

        return cleanSegment(value);
    }
}
