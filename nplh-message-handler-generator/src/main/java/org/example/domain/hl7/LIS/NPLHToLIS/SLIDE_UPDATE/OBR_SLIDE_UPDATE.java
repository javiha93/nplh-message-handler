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
    public static OBR_SLIDE_UPDATE FromMessage(Slide slide, Message message) {
        OBR_SLIDE_UPDATE slideUpdate = (OBR_SLIDE_UPDATE) OBR.FromMessage(slide, message, new OBR_SLIDE_UPDATE());

        slideUpdate.setArtifact("SLIDE");
        slideUpdate.setAction("StatusUpdate");

        return slideUpdate;
    }
    @Override
    public String toString() {
        String value = "OBR|||" +
                nullSafe(getSlideID()) + "|" +
                nullSafe(artifact) + "^" + nullSafe(action) + "|||" +
                nullSafe(getCollectDateTime()) + "||||||||||||" +
                nullSafe(getSlideID()) + "^^" + nullSafe(getExtSlideID()) + "|" +
                nullSafe(getBlockID()) + "^^" + nullSafe(getExtBlockID()) + "|" +
                nullSafe(getSpecimenID()) + "^^" + nullSafe(getExtSpecimenID()) + "|||||||||||||" +
                nullSafe(getTechnician()) + "^" + nullSafe(getTechnicianLN()) + "^" + nullSafe(getTechnicianFN()) + "^" + nullSafe(getTechnicianMN()) +  "|||||||||||||";

        return cleanSegment(value + getSupplementalInfo());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBR_SLIDE_UPDATE obr = (OBR_SLIDE_UPDATE) o;
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

    protected static OBR_SLIDE_UPDATE parseOBR(String line) {
        return (OBR_SLIDE_UPDATE) OBR.parseOBR(line, new OBR_SLIDE_UPDATE());
    }
}
