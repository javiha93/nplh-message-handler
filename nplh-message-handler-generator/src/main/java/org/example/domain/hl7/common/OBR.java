
package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.*;
import org.example.domain.message.entity.supplementalInfo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class OBR extends HL7Segment {

    //@HL7Position(position = 1)
    protected String segmentNumber;

    //@HL7Position(position = 2)
    protected String externalSampleId;

    //@HL7Position(position = 2)
    protected String sampleId;

    //@HL7Position(position = 4, subPosition = 1)
    protected String protocolNumber;

    //@HL7Position(position = 4, subPosition = 2)
    protected String protocolName;

    //@HL7Position(position = 4, subPosition = 3)
    protected String protocolIdentifier;

    //@HL7Position(position = 4, subPosition = 5)
    protected String protocolDescription;

    //@HL7Position(position = 7, subPosition = 1)
    protected String collectDateTime;

    //@HL7Position(position = 14)
    protected String receivedDateTime;

    //@HL7Position(position = 15, subPosition = 1)
    protected String tissueType;

    //@HL7Position(position = 15, subPosition = 2)
    protected String tissueDescription;

    //@HL7Position(position = 15, subPosition = 3)
    protected String surgicalName;

    //@HL7Position(position = 15, subPosition = 4)
    protected String surgicalDescription;

    //@HL7Position(position = 15, subPosition = 5)
    protected String anatomicSite;

    //@HL7Position(position = 15, subPosition = 6)
    protected String anatomicDescription;

    //@HL7Position(position = 15, subPosition = 7)
    protected String tissueSubtype;

    //@HL7Position(position = 15, subPosition = 8)
    protected String tissueSubtypeDesc;

    //@HL7Position(position = 16, subPosition = 1)
    protected String pathologist;

    //@HL7Position(position = 16, subPosition = 2)
    protected String pathologistLN;

    //@HL7Position(position = 16, subPosition = 3)
    protected String pathologistFN;

    //@HL7Position(position = 16, subPosition = 4)
    protected String pathologistMN;

    //@HL7Position(position = 16, subPosition = 5)
    protected String pathologistSuffix;

    //@HL7Position(position = 16, subPosition = 6)
    protected String pathologistPrefix;

    //@HL7Position(position = 16, subPosition = 7)
    protected String pathologistAddress1;

    //@HL7Position(position = 16, subPosition = 9)
    protected String pathologistCity;

    //@HL7Position(position = 16, subPosition = 10)
    protected String pathologistCountry;

    //@HL7Position(position = 16, subPosition = 11)
    protected String pathologistState;

    //@HL7Position(position = 16, subPosition = 12)
    protected String pathologistHomeTel;

    //@HL7Position(position = 16, subPosition = 13)
    protected String pathologistMobileTel;

    //@HL7Position(position = 16, subPosition = 15)
    protected String pathologistZip;

    //@HL7Position(position = 17, subPosition = 1)
    protected String pathologistWorkTel;

    //@HL7Position(position = 17, subPosition = 4)
    protected String pathologistMail;

    //@HL7Position(position = 18)
    protected String workFlow;

    //@HL7Position(position = 19, subPosition = 1)
    protected String slideID;

    //@HL7Position(position = 19, subPosition = 2)
    protected String sequence;

    //@HL7Position(position = 19, subPosition = 3)
    protected String extSlideID;

    //@HL7Position(position = 19, subPosition = 4)
    protected String textComment;

    //@HL7Position(position = 20, subPosition = 1)
    protected String blockID;

    //@HL7Position(position = 20, subPosition = 2)
    protected String sequenceBlock;

    //@HL7Position(position = 20, subPosition = 3)
    protected String extBlockID;

    //@HL7Position(position = 20, subPosition = 4)
    protected String parentBlockID;

    //@HL7Position(position = 21, subPosition = 1)
    protected String specimenID;

    //@HL7Position(position = 21, subPosition = 2)
    protected String sequenceSpecimen;

    //@HL7Position(position = 21, subPosition = 3)
    protected String extSpecimenID;

    //@HL7Position(position = 27, subPosition = 6)
    protected String stat;

    //@HL7Position(position = 27, subPosition = 8)
    protected String caseTags;

    //@HL7Position(position = 34, subPosition = 1)
    protected String technician;

    //@HL7Position(position = 34, subPosition = 2)
    protected String technicianLN;

    //@HL7Position(position = 34, subPosition = 3)
    protected String technicianFN;

    //@HL7Position(position = 34, subPosition = 4)
    protected String technicianMN;

    protected List<SupplementalInfo> supplementalInfos = new ArrayList<>();

    //@HL7Position(position = 47)
    protected String specialInstruction;

    //@HL7Position(position = 47)
    protected String specialInstructionArtifact;

    //@HL7Position(position = 47)
    protected String qualityIssue;

    //@HL7Position(position = 47)
    protected String qualityIssueArtifact;

    //@HL7Position(position = 47)
    protected String qualityIssueResolution;

    //@HL7Position(position = 47)
    protected String tissuePieces;

    //@HL7Position(position = 47)
    protected String tissuePiecesArtifact;

    //@HL7Position(position = 47)
    protected String recut;

    //@HL7Position(position = 47)
    protected String recutArtifact;

    //@HL7Position(position = 47)
    protected String grossDescription;

    //@HL7Position(position = 47)
    protected String grossDescriptionArtifact;


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

        obr = FromMessage(specimen, message, obr);

        if (order.getExtSampleId() != null) {
            obr.externalSampleId = order.getExtSampleId();
        } else {
            obr.externalSampleId = order.getSampleId() + " " + specimen.getSequence() + block.getSequence() + "-" + slide.getSequence();
        }
        obr.protocolNumber = slide.getStainProtocol().getNumber();
        obr.protocolName = slide.getStainProtocol().getName();
        obr.protocolIdentifier = slide.getStainProtocol().getIdentifier();
        obr.protocolDescription = slide.getStainProtocol().getDescription();
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
        obr.stat = order.getStat();

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

    protected static OBR parseOBR(String line, OBR obr) {
        String[] fields = line.split("\\|");

        // Campo 1 (position 1) - Segment Number
        if (fields.length > 1) {
            obr.setSegmentNumber(getFieldValue(fields, 1));
        }

        // Campo 2 (position 2) - External Sample ID
        if (fields.length > 2) {
            obr.setExternalSampleId(getFieldValue(fields, 2));
        }

        // Campo 4 (position 4) - Protocol (Code^Name^Identifier^^Description)
        if (fields.length > 4 && fields[4] != null && !fields[4].isEmpty()) {
            String[] protocol = fields[4].split("\\^");
            if (protocol.length > 0) obr.setProtocolNumber(protocol[0]);
            if (protocol.length > 1) obr.setProtocolName(protocol[1]);
            if (protocol.length > 2) obr.setProtocolIdentifier(protocol[2]);
            if (protocol.length > 4) obr.setProtocolDescription(protocol[4]);
        }

        // Campo 7 (position 7) - Collect Date/Time
        if (fields.length > 7) {
            obr.setCollectDateTime(getFieldValue(fields, 7));
        }

        // Campo 14 (position 14) - Received Date/Time
        if (fields.length > 14) {
            obr.setReceivedDateTime(getFieldValue(fields, 14));
        }

        // Campo 15 (position 15) - Specimen Source (TissueType^Description^SurgicalName^SurgicalDesc^AnatomicSite^AnatomicDesc^Subtype^SubtypeDesc)
        if (fields.length > 15 && fields[15] != null && !fields[15].isEmpty()) {
            String[] specimen = fields[15].split("\\^");
            if (specimen.length > 0) obr.setTissueType(specimen[0]);
            if (specimen.length > 1) obr.setTissueDescription(specimen[1]);
            if (specimen.length > 2) obr.setSurgicalName(specimen[2]);
            if (specimen.length > 3) obr.setSurgicalDescription(specimen[3]);
            if (specimen.length > 4) obr.setAnatomicSite(specimen[4]);
            if (specimen.length > 5) obr.setAnatomicDescription(specimen[5]);
            if (specimen.length > 6) obr.setTissueSubtype(specimen[6]);
            if (specimen.length > 7) obr.setTissueSubtypeDesc(specimen[7]);
        }

        // Pathologist
        if (fields.length > 16 && fields[16] != null && !fields[16].isEmpty()) {
            String[] pathologist = fields[16].split("\\^");
            if (pathologist.length > 0) obr.setPathologist(pathologist[0]);
            if (pathologist.length > 1) obr.setPathologistLN(pathologist[1]);
            if (pathologist.length > 2) obr.setPathologistFN(pathologist[2]);
            if (pathologist.length > 3) obr.setPathologistMN(pathologist[3]);
            if (pathologist.length > 4) obr.setPathologistSuffix(pathologist[4]);
            if (pathologist.length > 5) obr.setPathologistPrefix(pathologist[5]);
            if (pathologist.length > 6) obr.setPathologistAddress1(pathologist[6]);
            if (pathologist.length > 8) obr.setPathologistCity(pathologist[8]);
            if (pathologist.length > 9) obr.setPathologistCountry(pathologist[9]);
            if (pathologist.length > 10) obr.setPathologistState(pathologist[10]);
            if (pathologist.length > 11) obr.setPathologistHomeTel(pathologist[11]);
            if (pathologist.length > 12) obr.setPathologistMobileTel(pathologist[12]);
            if (pathologist.length > 14) obr.setPathologistZip(pathologist[14]);
        }

        if (fields.length > 17 && fields[17] != null && !fields[17].isEmpty()) {
            String[] pathologist = fields[17].split("\\^");
            if (pathologist.length > 0) obr.setPathologistWorkTel(pathologist[0]);
            if (pathologist.length > 3) obr.setPathologistMail(pathologist[3]);
        }

        // Campo 18 (position 18) - Workflow
        if (fields.length > 18) {
            obr.setWorkFlow(getFieldValue(fields, 18));
        }

        // Campo 19 (position 19) - Slide Info (SlideID^Sequence^ExtSlideID^TextComment)
        if (fields.length > 19 && fields[19] != null && !fields[19].isEmpty()) {
            String[] slide = fields[19].split("\\^");
            if (slide.length > 0) obr.setSlideID(slide[0]);
            if (slide.length > 1) obr.setSequence(slide[1]);
            if (slide.length > 2) obr.setExtSlideID(slide[2]);
            if (slide.length > 3) obr.setTextComment(slide[3]);
        }

        // Campo 20 (position 20) - Block Info (BlockID^SequenceBlock^ExtBlockID^ParentBlockID)
        if (fields.length > 20 && fields[20] != null && !fields[20].isEmpty()) {
            String[] block = fields[20].split("\\^");
            if (block.length > 0) obr.setBlockID(block[0]);
            if (block.length > 1) obr.setSequenceBlock(block[1]);
            if (block.length > 2) obr.setExtBlockID(block[2]);
            if (block.length > 3) obr.setParentBlockID(block[3]);
        }

        // Campo 21 (position 21) - Specimen Info (SpecimenID^SequenceSpecimen^ExtSpecimenID)
        if (fields.length > 21 && fields[21] != null && !fields[21].isEmpty()) {
            String[] specimenInfo = fields[21].split("\\^");
            if (specimenInfo.length > 0) obr.setSpecimenID(specimenInfo[0]);
            if (specimenInfo.length > 1) obr.setSequenceSpecimen(specimenInfo[1]);
            if (specimenInfo.length > 2) obr.setExtSpecimenID(specimenInfo[2]);
        }

        //Technician
        if (fields.length > 34 && fields[34] != null && !fields[34].isEmpty()) {
            String[] technician = fields[34].split("\\^");
            if (technician.length > 0) obr.setTechnician(technician[0]);
            if (technician.length > 1) obr.setTechnicianLN(technician[1]);
            if (technician.length > 2) obr.setTechnicianFN(technician[2]);
            if (technician.length > 3) obr.setTechnicianMN(technician[3]);
        }

        return obr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBR obr = (OBR) o;
        return Objects.equals(segmentNumber, obr.segmentNumber) &&
                Objects.equals(externalSampleId, obr.externalSampleId) &&
                Objects.equals(sampleId, obr.sampleId) &&
                Objects.equals(protocolNumber, obr.protocolNumber) &&
                Objects.equals(protocolName, obr.protocolName) &&
                Objects.equals(protocolIdentifier, obr.protocolIdentifier) &&
                Objects.equals(protocolDescription, obr.protocolDescription) &&
                Objects.equals(collectDateTime, obr.collectDateTime) &&
                Objects.equals(receivedDateTime, obr.receivedDateTime) &&
                Objects.equals(tissueType, obr.tissueType) &&
                Objects.equals(tissueDescription, obr.tissueDescription) &&
                Objects.equals(surgicalName, obr.surgicalName) &&
                Objects.equals(surgicalDescription, obr.surgicalDescription) &&
                Objects.equals(anatomicSite, obr.anatomicSite) &&
                Objects.equals(anatomicDescription, obr.anatomicDescription) &&
                Objects.equals(tissueSubtype, obr.tissueSubtype) &&
                Objects.equals(tissueSubtypeDesc, obr.tissueSubtypeDesc) &&
                Objects.equals(pathologist, obr.pathologist) &&
                Objects.equals(pathologistLN, obr.pathologistLN) &&
                Objects.equals(pathologistFN, obr.pathologistFN) &&
                Objects.equals(pathologistMN, obr.pathologistMN) &&
                Objects.equals(pathologistSuffix, obr.pathologistSuffix) &&
                Objects.equals(pathologistPrefix, obr.pathologistPrefix) &&
                Objects.equals(pathologistAddress1, obr.pathologistAddress1) &&
                Objects.equals(pathologistCity, obr.pathologistCity) &&
                Objects.equals(pathologistCountry, obr.pathologistCountry) &&
                Objects.equals(pathologistState, obr.pathologistState) &&
                Objects.equals(pathologistHomeTel, obr.pathologistHomeTel) &&
                Objects.equals(pathologistMobileTel, obr.pathologistMobileTel) &&
                Objects.equals(pathologistZip, obr.pathologistZip) &&
                Objects.equals(pathologistWorkTel, obr.pathologistWorkTel) &&
                Objects.equals(pathologistMail, obr.pathologistMail) &&
                Objects.equals(workFlow, obr.workFlow) &&
                Objects.equals(slideID, obr.slideID) &&
                Objects.equals(sequence, obr.sequence) &&
                Objects.equals(extSlideID, obr.extSlideID) &&
                Objects.equals(textComment, obr.textComment) &&
                Objects.equals(blockID, obr.blockID) &&
                Objects.equals(sequenceBlock, obr.sequenceBlock) &&
                Objects.equals(extBlockID, obr.extBlockID) &&
                Objects.equals(parentBlockID, obr.parentBlockID) &&
                Objects.equals(specimenID, obr.specimenID) &&
                Objects.equals(sequenceSpecimen, obr.sequenceSpecimen) &&
                Objects.equals(extSpecimenID, obr.extSpecimenID) &&
                Objects.equals(stat, obr.stat) &&
                Objects.equals(caseTags, obr.caseTags) &&
                Objects.equals(technician, obr.technician) &&
                Objects.equals(technicianLN, obr.technicianLN) &&
                Objects.equals(technicianFN, obr.technicianFN) &&
                Objects.equals(technicianMN, obr.technicianMN) &&
                Objects.equals(supplementalInfos, obr.supplementalInfos) &&
                Objects.equals(specialInstruction, obr.specialInstruction) &&
                Objects.equals(specialInstructionArtifact, obr.specialInstructionArtifact) &&
                Objects.equals(qualityIssue, obr.qualityIssue) &&
                Objects.equals(qualityIssueArtifact, obr.qualityIssueArtifact) &&
                Objects.equals(qualityIssueResolution, obr.qualityIssueResolution) &&
                Objects.equals(tissuePieces, obr.tissuePieces) &&
                Objects.equals(tissuePiecesArtifact, obr.tissuePiecesArtifact) &&
                Objects.equals(recut, obr.recut) &&
                Objects.equals(recutArtifact, obr.recutArtifact) &&
                Objects.equals(grossDescription, obr.grossDescription) &&
                Objects.equals(grossDescriptionArtifact, obr.grossDescriptionArtifact);
    }

}
