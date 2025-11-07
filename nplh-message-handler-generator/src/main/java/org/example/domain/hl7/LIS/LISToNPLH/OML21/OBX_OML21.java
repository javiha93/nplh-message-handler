package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.OBX;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class OBX_OML21 extends OBX {

    public static OBX_OML21 Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBX_OML21) OBX.Default(sampleId, specimenId, blockId, slideId, segmentNumber, new OBX_OML21());
    }

    public static OBX_OML21 FromMessage(Slide slide, Message message, int segmentNumber) {
        return (OBX_OML21) OBX.FromMessage(slide, message, segmentNumber, new OBX_OML21());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
