package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import org.example.domain.hl7.common.OBR;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.Objects;

@Data
public class OBR_SLIDE_UPDATE extends OBR {

    String artifact;
    String action;
    public static OBR_SLIDE_UPDATE fromSlide(Slide slide, Message message) {
        OBR_SLIDE_UPDATE slideUpdate = (OBR_SLIDE_UPDATE) OBR.FromMessage(slide, message, new OBR_SLIDE_UPDATE());

        slideUpdate.setArtifact("SLIDE");
        slideUpdate.setAction("StatusUpdate");

        return slideUpdate;
    }

    public static OBR_SLIDE_UPDATE fromSlideVSSUpdate(Slide slide, Message message) {
        OBR_SLIDE_UPDATE slideUpdate = (OBR_SLIDE_UPDATE) OBR.FromMessage(slide, message, 1, new OBR_SLIDE_UPDATE());

        return slideUpdate;
    }
    @Override
    public String toString() {
        String value;
        if (getAction() == null || getAction().isEmpty()) {
            value = "OBR|" +
                    nullSafe(segmentNumber) + "|" +
                    nullSafe(protocolNumber) + "^" + nullSafe(protocolName) + "^" + nullSafe(protocolIdentifier) + "|||" +
                    nullSafe(collectDateTime) + "||||||||" +
                    nullSafe(tissueType) + "^" + nullSafe(tissueDescription) + "^" + nullSafe(surgicalName) +  "^" + nullSafe(surgicalDescription) + "|" +
                    nullSafe(pathologist) + "^" + nullSafe(pathologistLN) + "^" + nullSafe(pathologistFN) + "^" + nullSafe(pathologistMN) + "|||" +
                    nullSafe(slideID) + "^" + nullSafe(sequence) + "^" + nullSafe(extSlideID) + "|" +
                    nullSafe(blockID) + "^" + nullSafe(sequenceBlock) + "^" + nullSafe(extBlockID) + "|" +
                    nullSafe(specimenID) + "^" + nullSafe(sequenceSpecimen) + "^" + nullSafe(extSpecimenID) + "|";
        } else {
            value = "OBR|||" +
                    nullSafe(getSlideID()) + "|" +
                    nullSafe(artifact) + "^" + nullSafe(action) + "|||" +
                    nullSafe(getCollectDateTime()) + "||||||||||||" +
                    nullSafe(getSlideID()) + "^^" + nullSafe(getExtSlideID()) + "|" +
                    nullSafe(getBlockID()) + "^^" + nullSafe(getExtBlockID()) + "|" +
                    nullSafe(getSpecimenID()) + "^^" + nullSafe(getExtSpecimenID()) + "|||||||||||||" +
                    nullSafe(getTechnician()) + "^" + nullSafe(getTechnicianLN()) + "^" + nullSafe(getTechnicianFN()) + "^" + nullSafe(getTechnicianMN()) +  "|||||||||||||";
        }

        return cleanSegment(value + getSupplementalInfo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBR_SLIDE_UPDATE obr = (OBR_SLIDE_UPDATE) o;

        if (getAction() == null || getAction().isEmpty()) {
            return Objects.equals(protocolNumber, obr.protocolNumber) &&
                    Objects.equals(protocolName, obr.protocolName) &&
                    Objects.equals(protocolIdentifier, obr.protocolIdentifier) &&
                    Objects.equals(tissueType, obr.tissueType) &&
                    Objects.equals(tissueDescription, obr.tissueDescription) &&
                    Objects.equals(surgicalName, obr.surgicalName) &&
                    Objects.equals(surgicalDescription, obr.surgicalDescription) &&
                    Objects.equals(pathologist, obr.pathologist) &&
                    Objects.equals(pathologistFN, obr.pathologistFN) &&
                    Objects.equals(pathologistLN, obr.pathologistLN) &&
                    Objects.equals(pathologistMN, obr.pathologistMN) &&
                    Objects.equals(slideID, obr.slideID) &&
                    Objects.equals(sequence, obr.sequence) &&
                    Objects.equals(extSlideID, obr.extSlideID) &&
                    Objects.equals(blockID, obr.blockID) &&
                    Objects.equals(sequenceBlock, obr.sequenceBlock) &&
                    Objects.equals(extBlockID, obr.extBlockID) &&
                    Objects.equals(specimenID, obr.specimenID) &&
                    Objects.equals(sequenceSpecimen, obr.sequenceSpecimen) &&
                    Objects.equals(extSpecimenID, obr.extSpecimenID);
        }

        return Objects.equals(slideID, obr.slideID) &&
                Objects.equals(extSlideID, obr.extSlideID) &&
                Objects.equals(blockID, obr.blockID) &&
                Objects.equals(extBlockID, obr.extBlockID) &&
                Objects.equals(specimenID, obr.specimenID) &&
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

    protected static OBR_SLIDE_UPDATE parseVSSOBR(String line) {
        OBR_SLIDE_UPDATE obr = (OBR_SLIDE_UPDATE) OBR.parseOBR(line, new OBR_SLIDE_UPDATE());

        String[] fields = line.split("\\|");

        if (fields.length > 1) {
            obr.setSegmentNumber(getFieldValue(fields, 1));
        }

        // Campo 4 (position 4) - Protocol (Code^Name^Identifier^^Description)
        if (fields.length > 4 && fields[4] != null && !fields[4].isEmpty()) {
            String[] protocol = fields[4].split("\\^");
            if (protocol.length > 0) obr.setProtocolNumber(protocol[0]);
            if (protocol.length > 1) obr.setProtocolName(protocol[1]);
            if (protocol.length > 2) obr.setProtocolDescription(protocol[2]);
        }

        // Campo 7 (position 7) - Collect Date/Time
        if (fields.length > 7) {
            obr.setCollectDateTime(getFieldValue(fields, 7));
        }

        // Campo 15 (position 15) - Specimen Source (TissueType^Description^SurgicalName^SurgicalDesc^AnatomicSite^AnatomicDesc^Subtype^SubtypeDesc)
        if (fields.length > 15 && fields[15] != null && !fields[15].isEmpty()) {
            String[] specimen = fields[15].split("\\^");
            if (specimen.length > 0) obr.setTissueType(specimen[0]);
            if (specimen.length > 1) obr.setTissueDescription(specimen[1]);
            if (specimen.length > 2) obr.setSurgicalName(specimen[2]);
            if (specimen.length > 3) obr.setSurgicalDescription(specimen[3]);
        }

        // Campo 16 (position 16) - Pathologist
        if (fields.length > 16 && fields[16] != null && !fields[16].isEmpty()) {
            String[] pathologist = fields[16].split("\\^");
            if (pathologist.length > 0) obr.setPathologist(pathologist[0]);
            if (pathologist.length > 1) obr.setPathologistLN(pathologist[1]);
            if (pathologist.length > 2) obr.setPathologistFN(pathologist[2]);
            if (pathologist.length > 3) obr.setPathologistMN(pathologist[3]);
        }

        // Campo 19 (position 19) - Slide Info (SlideID^Sequence^ExtSlideID^TextComment)
        if (fields.length > 19 && fields[19] != null && !fields[19].isEmpty()) {
            String[] slide = fields[19].split("\\^");
            if (slide.length > 0) obr.setSlideID(slide[0]);
            if (slide.length > 1) obr.setSequence(slide[1]);
            if (slide.length > 2) obr.setExtSlideID(slide[2]);
        }

        // Campo 20 (position 20) - Block Info (BlockID^SequenceBlock^ExtBlockID^ParentBlockID)
        if (fields.length > 20 && fields[20] != null && !fields[20].isEmpty()) {
            String[] block = fields[20].split("\\^");
            if (block.length > 0) obr.setBlockID(block[0]);
            if (block.length > 1) obr.setSequenceBlock(block[1]);
            if (block.length > 2) obr.setExtBlockID(block[2]);
        }

        // Campo 21 (position 21) - Specimen Info (SpecimenID^SequenceSpecimen^ExtSpecimenID)
        if (fields.length > 21 && fields[21] != null && !fields[21].isEmpty()) {
            String[] specimenInfo = fields[21].split("\\^");
            if (specimenInfo.length > 0) obr.setSpecimenID(specimenInfo[0]);
            if (specimenInfo.length > 1) obr.setSequenceSpecimen(specimenInfo[1]);
            if (specimenInfo.length > 2) obr.setExtSpecimenID(specimenInfo[2]);
        }

        return obr;
    }

    protected static OBR_SLIDE_UPDATE parseOBR(String line) {
        OBR_SLIDE_UPDATE obr = (OBR_SLIDE_UPDATE) OBR.parseOBR(line, new OBR_SLIDE_UPDATE());

        String[] fields = line.split("\\|");

        // Campo 4 (position 4) - Protocol (Code^Name^Identifier^^Description)
        if (fields.length > 4 && fields[4] != null && !fields[4].isEmpty()) {
            String[] messageProp = fields[4].split("\\^");
            if (messageProp.length > 0) obr.setArtifact(messageProp[0]);
            if (messageProp.length > 1) obr.setAction(messageProp[1]);
        }

        // Campo 7 (position 7) - Collect Date/Time
        if (fields.length > 7) {
            obr.setCollectDateTime(getFieldValue(fields, 7));
        }

        // Campo 19 (position 19) - Slide Info (SlideID^Sequence^ExtSlideID^TextComment)
        if (fields.length > 19 && fields[19] != null && !fields[19].isEmpty()) {
            String[] slide = fields[19].split("\\^");
            if (slide.length > 0) obr.setSlideID(slide[0]);
            if (slide.length > 2) obr.setExtSlideID(slide[2]);
        }

        // Campo 20 (position 20) - Block Info (BlockID^SequenceBlock^ExtBlockID^ParentBlockID)
        if (fields.length > 20 && fields[20] != null && !fields[20].isEmpty()) {
            String[] block = fields[20].split("\\^");
            if (block.length > 0) obr.setBlockID(block[0]);
            if (block.length > 2) obr.setExtBlockID(block[2]);
        }

        // Campo 21 (position 21) - Specimen Info (SpecimenID^SequenceSpecimen^ExtSpecimenID)
        if (fields.length > 21 && fields[21] != null && !fields[21].isEmpty()) {
            String[] specimenInfo = fields[21].split("\\^");
            if (specimenInfo.length > 0) obr.setSpecimenID(specimenInfo[0]);
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
}
