package org.example.domain.hl7.VTG.VTGToNPLH.ADDITION;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class OBR extends org.example.domain.hl7.common.OBR {

    private String entity;
    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.Default(sampleId, specimenId, blockId, slideId, segmentNumber, new OBR());
    }

    public static OBR FromMessage(Slide slide, Message message, int segmentNumber) {
        OBR obr = (OBR) org.example.domain.hl7.common.OBR.FromMessage(slide, message, segmentNumber, new OBR());
        obr.setEntity("SLIDE");
        return obr;
    }

    public static OBR FromMessage(Block block, Message message, int segmentNumber) {
        OBR obr = (OBR) org.example.domain.hl7.common.OBR.FromMessage(block, message, segmentNumber, new OBR());
        obr.setEntity("BLOCK");
        return obr;
    }
    @Override
    public String toString() {
        String value = "OBR|" +
                nullSafe(getSegmentNumber()) + "||" +
                nullSafe(getSampleId()) + "||||" +
                nullSafe(getCollectDateTime()) + "||||||||" +
                nullSafe(getTissueType()) + "^" + nullSafe(getTissueDescription()) + "^" + nullSafe(getSurgicalName()) + "^" + nullSafe(getSurgicalDescription()) + "||||" +
                nullSafe(getSlideID()) + "^" + nullSafe(getSequence()) + "^" + nullSafe(getExtSlideID()) + "|" +
                nullSafe(getBlockID()) + "^" + nullSafe(getSequenceBlock()) + "^" + nullSafe(getExtBlockID()) + "|" +
                nullSafe(getSpecimenID()) + "^" + nullSafe(getSequenceSpecimen()) + "^" + nullSafe(getExtSpecimenID()) + "|||||||||||||" +
                nullSafe(getTechnician()) + "^" + nullSafe(getTechnicianLN()) + "^" + nullSafe(getTechnicianFN()) + "^" + nullSafe(getTechnicianMN()) +  "|||||||||||||" +
                "ADDITION^^" + getEntity();

        return cleanSegment(value);
    }
}
