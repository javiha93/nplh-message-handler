package org.example.domain.hl7.VTG.VTGToNPLH.OEWF;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setActionCode("SN");

        return orc;
    }

    public static ORC FromMessage(Slide slide, Message message) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.FromMessage(slide, message, new ORC());

        orc.setActionCode("SN");

        return orc;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
