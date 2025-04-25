package org.example.domain.hl7.VTG.VTGToNPLH.OEWF;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.SupplementalInfo;
import org.example.domain.message.entity.supplementalInfo.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OBR extends org.example.domain.hl7.common.OBR {

    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.Default(sampleId, specimenId, blockId, slideId, segmentNumber);
    }

    public static OBR FromMessage(Slide slide, Message message, int segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.FromMessage(slide, message, segmentNumber, new OBR());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
