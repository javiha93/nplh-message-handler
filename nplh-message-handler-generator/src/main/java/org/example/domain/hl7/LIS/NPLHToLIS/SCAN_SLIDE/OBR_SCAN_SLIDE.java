package org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE;

import lombok.Data;
import org.example.domain.hl7.common.OBR;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.Objects;

@Data
public class OBR_SCAN_SLIDE extends OBR {

    protected String artifact;

    protected String action;

    public static OBR_SCAN_SLIDE fromSlide(Slide slide, Message message)
    {
        OBR_SCAN_SLIDE obr = new OBR_SCAN_SLIDE();
        obr.setSlideID(slide.getId());
        obr.setArtifact("SLIDE");
        obr.setAction("sendScannedSlideImageLabelId");

        return obr;
    }

    @Override
    public String toString() {
        String value = "OBR|1||" +
                nullSafe(slideID) +            // 3
                "|" + nullSafe(artifact) + "^" + nullSafe(action);

        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBR_SCAN_SLIDE obr = (OBR_SCAN_SLIDE) o;
        return Objects.equals(slideID, obr.slideID) &&
                Objects.equals(artifact, obr.artifact) &&
                Objects.equals(action, obr.action);
    }

    protected static OBR_SCAN_SLIDE parseOBR(String line) {
        OBR_SCAN_SLIDE obr = new OBR_SCAN_SLIDE();
        String[] fields = line.split("\\|");

        if (fields.length > 3) {
            obr.setSlideID(getFieldValue(fields, 3));
        }

        if (fields.length > 4 && fields[4] != null && !fields[4].isEmpty()) {
            String[] scanProps = fields[4].split("\\^");
            if (scanProps.length > 0) obr.setArtifact(scanProps[0]);
            if (scanProps.length > 1) obr.setAction(scanProps[1]);
        }

        if (!obr.action.equals("sendScannedSlideImageLabelId")) {
            throw new RuntimeException("Unable to parse to SCAN_SLIDE");
        }

        return obr;
    }
}
