package org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID, String slideStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setSlideStatus(slideStatus);

        return orc;
    }

    public static ORC FromMessage(Slide slide, Message message, String slideStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.FromMessage(slide, message, new ORC());
        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setSlideStatus(slideStatus);

        return orc;
    }

    @Override
    public String toString() {
            String value = "ORC|" +
                    nullSafe(getMessageCode()) + "||" +        // 1
                    nullSafe(getSlideId()) + "||" +      // 3
                    nullSafe(getActionCode()) + "||||||||||||||||||||" + // 5
                    nullSafe(getSlideStatus()) + "|";         // 21

            return cleanSegment(value);
    }
}
