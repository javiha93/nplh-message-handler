package org.example.domain.hl7.VTG.NPLHToVTG.OML21;

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

    public static OBR_OML21 fromMessage(Slide slide, Message message, int segmentNumber) {
        OBR_OML21 obr = (OBR_OML21) OBR.FromMessage(slide, message, segmentNumber, new OBR_OML21());
        obr.externalSampleId = message.getOrder().getSampleId();

        obr.pathologistAddress1 = null;
        obr.pathologistCity = null;
        obr.pathologistCountry = null;
        obr.pathologistState = null;
        obr.pathologistHomeTel = null;
        obr.pathologistMobileTel = null;
        obr.pathologistWorkTel = null;
        obr.pathologistZip = null;
        obr.pathologistMail = null;

        return obr;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    protected static OBR_OML21 parseOBR(String line) {
        return (OBR_OML21) OBR.parseOBR(line, new OBR_OML21());
    }
}
