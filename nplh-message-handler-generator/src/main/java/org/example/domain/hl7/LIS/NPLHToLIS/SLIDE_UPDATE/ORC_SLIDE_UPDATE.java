package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import org.example.domain.hl7.common.ORC;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.Objects;

@Data
public class ORC_SLIDE_UPDATE extends ORC {

    public static ORC_SLIDE_UPDATE FromMessage(Slide slide, Message message, String slideStatus) {
        ORC_SLIDE_UPDATE orc = (ORC_SLIDE_UPDATE) ORC.fromMessage(slide, message, new ORC_SLIDE_UPDATE());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ORC_SLIDE_UPDATE orc = (ORC_SLIDE_UPDATE) o;
        return Objects.equals(actionCode, orc.actionCode) &&
                Objects.equals(messageCode, orc.messageCode) &&
                Objects.equals(slideId, orc.slideId) &&
                Objects.equals(slideStatus, orc.slideStatus);
    }

    protected static ORC_SLIDE_UPDATE parseORC(String line) {
        ORC_SLIDE_UPDATE orc = new ORC_SLIDE_UPDATE();

        String[] fields = line.split("\\|");

        if (fields.length > 1) {
            orc.setMessageCode(getFieldValue(fields, 1));
        }

        if (fields.length > 3) {
            orc.setSlideId(getFieldValue(fields, 3));
        }

        if (fields.length > 5) {
            orc.setActionCode(getFieldValue(fields, 5));
        }

        if (fields.length > 25) {
            orc.setSlideStatus(getFieldValue(fields, 25));
        }

        return orc;

    }
}
