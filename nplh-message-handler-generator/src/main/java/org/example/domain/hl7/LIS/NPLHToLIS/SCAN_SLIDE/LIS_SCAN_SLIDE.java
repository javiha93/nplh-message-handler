package org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE;

import lombok.Data;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.Objects;

import static org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.MSH_SCAN_SLIDE.parseMSH;
import static org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.OBR_SCAN_SLIDE.parseOBR;
import static org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.ORC_SCAN_SLIDE.parseORC;

@Data
public class LIS_SCAN_SLIDE extends HL7Segment implements HL7Message {
    MSH_SCAN_SLIDE msh;
    ORC_SCAN_SLIDE orc;
    OBR_SCAN_SLIDE obr;

    public static LIS_SCAN_SLIDE fromMessage(Slide slide, Message message) {
        LIS_SCAN_SLIDE scanSlide = new LIS_SCAN_SLIDE();

        scanSlide.msh = MSH_SCAN_SLIDE.fromMessageHeader(message.getHeader());
        scanSlide.orc = ORC_SCAN_SLIDE.fromSlide(slide);
        scanSlide.obr = OBR_SCAN_SLIDE.fromSlide(slide, message);

        return scanSlide;
    }

    @Override
    public String toString() {
        return nullSafe(msh) + "\n" +
                nullSafe(orc) + "\n" +
                nullSafe(obr) + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LIS_SCAN_SLIDE scanSlide = (LIS_SCAN_SLIDE) o;
        return Objects.equals(msh, scanSlide.msh) &&
                Objects.equals(orc, scanSlide.orc) &&
                Objects.equals(obr, scanSlide.obr);
    }

    public static LIS_SCAN_SLIDE fromString(String hl7Message) {
        if (hl7Message == null || hl7Message.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }

        LIS_SCAN_SLIDE scanSlide = new LIS_SCAN_SLIDE();

        String[] lines = hl7Message.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String segmentType = line.substring(0, 3);

            switch (segmentType) {
                case "MSH":
                    scanSlide.msh = parseMSH(line);
                    break;
                case "ORC":
                    scanSlide.orc = parseORC(line);
                    break;
                case "OBR":
                    scanSlide.obr = parseOBR(line);
                    break;
            }
        }

        return scanSlide;
    }
}
