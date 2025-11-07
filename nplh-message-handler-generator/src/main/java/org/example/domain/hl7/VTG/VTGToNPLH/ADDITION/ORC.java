package org.example.domain.hl7.VTG.VTGToNPLH.ADDITION;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID, String blockStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("SN");

        return orc;
    }

    public static ORC FromMessage(Slide slide, Message message) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.FromMessage(slide, message, new ORC());
        orc.setMessageCode("SN");

        return orc;
    }

    public static ORC FromMessage(Block block, Message message) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.FromMessage(block, message, new ORC());
        orc.setMessageCode("SN");

        return orc;
    }

    @Override
    public String toString() {
            String value = "ORC|" +
                    nullSafe(getMessageCode()) + "||" +        // 1
                    nullSafe(getSampleID()) + "||||||||||||||||||" +      // 3
                    nullSafe(getFacilityCode()) + "^" + nullSafe(getFacilityName());  // 21

            return cleanSegment(value);
    }
}
