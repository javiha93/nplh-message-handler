package org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

public class ORC extends org.example.domain.hl7.common.ORC {


    public static ORC Default(String sampleID, String slideId) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("CA");
        orc.setActionCode("CA");
        orc.setSlideId(slideId);
        orc.setSlideStatus("DELETE");

        return orc;
    }

    public static ORC FromMessage(Slide slide, Message message) {
        ORC orc = (ORC) fromMessage(slide, message, new ORC());

        orc.setMessageCode("CA");
        orc.setActionCode("CA");
        orc.setOrderStatus("DELETE");

        return orc;
    }

    @Override
    public String toString() {
        String value = "ORC|" +
                nullSafe(getMessageCode()) + "|" +        // 1
                nullSafe(getSampleID()) + "||" +          // 2
                nullSafe(getSlideId()) + "^SLIDE|" +      // 4
                nullSafe(getActionCode()) + "||||||||||||||||||||" +           // 5
                nullSafe(getOrderStatus()) + "|";         // 21

        return cleanSegment(value);
    }
}
