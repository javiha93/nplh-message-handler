package org.example.domain.hl7.VTG.NPLHToVTG;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.OBR;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class OBR_OML21 extends OBR {
    public static OBR_OML21 Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        return (OBR_OML21) OBR.Default(sampleId, specimenId, blockId, slideId, segmentNumber, new OBR_OML21());
    }

    public static OBR_OML21 FromMessage(Slide slide, Message message, int segmentNumber) {
        return (OBR_OML21) OBR.FromMessage(slide, message, segmentNumber, new OBR_OML21());
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
