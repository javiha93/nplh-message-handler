package org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Specimen;

@Data
@NoArgsConstructor
public class OBR extends org.example.domain.hl7.common.OBR {
    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.Default(sampleId, specimenId, blockId, slideId, segmentNumber, new OBR());
    }

    public static OBR FromMessage(Specimen specimen, Message message) {
        return (OBR) org.example.domain.hl7.common.OBR.FromMessage(specimen, message, new OBR());
    }
    @Override
    public String toString() {
        String value = "OBR|||" +
                nullSafe(getBlockID()) + "|" +
                "PART^StatusUpdate|||" +
                nullSafe(getCollectDateTime()) + "||||||||||||" +
                nullSafe(getSlideID()) + "^" + nullSafe(getSequence()) + "^" + nullSafe(getExtSlideID()) + "|" +
                nullSafe(getBlockID()) + "^" + nullSafe(getSequenceBlock()) + "^" + nullSafe(getExtBlockID()) + "|" +
                nullSafe(getSpecimenID()) + "^" + nullSafe(getSequenceSpecimen()) + "^" + nullSafe(getExtSpecimenID()) + "|||||||||||||" +
                nullSafe(getTechnician()) + "^" + nullSafe(getTechnicianLN()) + "^" + nullSafe(getTechnicianFN()) + "^" + nullSafe(getTechnicianMN()) +  "|||||||||||||";

        return cleanSegment(value + getSupplementalInfo());
    }
}
