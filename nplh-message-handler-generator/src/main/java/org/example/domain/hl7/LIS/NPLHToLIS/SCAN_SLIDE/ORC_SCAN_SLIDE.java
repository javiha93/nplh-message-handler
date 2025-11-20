package org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.ORC;
import org.example.domain.message.entity.Slide;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ORC_SCAN_SLIDE extends ORC {

    public static ORC_SCAN_SLIDE fromSlide(Slide slide)
    {
        ORC_SCAN_SLIDE orc = new ORC_SCAN_SLIDE();
        orc.setActionCode("RE");
        orc.setSlideId(slide.getId());

        return orc;
    }

    @Override
    public String toString() {
        String value = "ORC|" +
                nullSafe(actionCode) + "||" +                                                    // 1
                nullSafe(slideId);                                                               // 3

        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ORC_SCAN_SLIDE orc = (ORC_SCAN_SLIDE) o;
        return Objects.equals(slideId, orc.slideId) &&
                Objects.equals(actionCode, orc.actionCode);
    }

    protected static ORC_SCAN_SLIDE parseORC(String line) {
        ORC_SCAN_SLIDE orc = new ORC_SCAN_SLIDE();
        String[] fields = line.split("\\|");

        if (fields.length > 1) {
            orc.setActionCode(getFieldValue(fields, 1));
        }

        if (fields.length > 3) {
            orc.setSlideId(getFieldValue(fields, 3));
        }

        return orc;
    }
}
