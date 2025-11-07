package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class OBR extends org.example.domain.hl7.common.OBR {
    public static OBR Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.Default(sampleId, specimenId, blockId, slideId, segmentNumber, new OBR());
    }

    public static OBR FromMessage(Slide slide, Message message, int segmentNumber) {
        return (OBR) org.example.domain.hl7.common.OBR.FromMessage(slide, message, segmentNumber, new OBR());
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
