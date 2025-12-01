package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.Objects;

import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.OBR_SLIDE_UPDATE.parseOBR;
import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.OBR_SLIDE_UPDATE.parseVSSOBR;
import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.ORC_SLIDE_UPDATE.parseORC;
import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.ORC_SLIDE_UPDATE.parseORCVSSUpdate;
import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.PID_SLIDE_UPDATE.parsePID;
import static org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.SAC_SLIDE_UPDATE.parseSAC;

@Data
public class LIS_SLIDE_UPDATE extends HL7Segment implements HL7Message {
    MSH_SLIDE_UPDATE msh;
    PID_SLIDE_UPDATE pid;
    SAC_SLIDE_UPDATE sac;
    ORC_SLIDE_UPDATE orc;
    OBR_SLIDE_UPDATE obr;
    OBX_SLIDE_UPDATE obx;

    public static LIS_SLIDE_UPDATE fromVTGUpdate(Message message, Slide slide, String slideStatus) {
        LIS_SLIDE_UPDATE slideUpdate = new LIS_SLIDE_UPDATE();

        slideUpdate.msh = MSH_SLIDE_UPDATE.fromMessageHeader(message.getHeader());
        slideUpdate.pid = PID_SLIDE_UPDATE.fromPatient(message.getPatient());
        slideUpdate.sac = SAC_SLIDE_UPDATE.fromOrder(message.getOrder());
        slideUpdate.orc = ORC_SLIDE_UPDATE.fromSlide(slide, message, slideStatus);
        slideUpdate.obr = OBR_SLIDE_UPDATE.fromSlide(slide, message);

        //updateSlideStatus(slide, slideStatus);

        return slideUpdate;
    }

    public static LIS_SLIDE_UPDATE fromVSSUpdate(Message message, Slide slide) {
        LIS_SLIDE_UPDATE slideUpdate = new LIS_SLIDE_UPDATE();

        Block block = slide.getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);

        slideUpdate.msh = MSH_SLIDE_UPDATE.fromMessageHeader(message.getHeader());
        slideUpdate.pid = PID_SLIDE_UPDATE.fromPatient(message.getPatient());
        slideUpdate.sac = SAC_SLIDE_UPDATE.fromOrder(message.getOrder());
        slideUpdate.orc = ORC_SLIDE_UPDATE.fromSpecimen(specimen, message);
        slideUpdate.obr = OBR_SLIDE_UPDATE.fromSlideVSSUpdate(slide, message);
        slideUpdate.obx = OBX_SLIDE_UPDATE.fromSlide(slide, message);

        return slideUpdate;
    }

    @Override
    public String toString() {
        String slideUpdate = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(sac) + "\n" +
                nullSafe(orc) + "\n" +
                nullSafe(obr) + "\n" +
                nullSafe(obx);

        return cleanMessage(slideUpdate );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LIS_SLIDE_UPDATE slideUpdate = (LIS_SLIDE_UPDATE) o;
        return Objects.equals(msh, slideUpdate.msh) &&
                Objects.equals(pid, slideUpdate.pid) &&
                Objects.equals(orc, slideUpdate.orc) &&
                Objects.equals(obr, slideUpdate.obr) &&
                Objects.equals(obx, slideUpdate.obx);
    }

    public static LIS_SLIDE_UPDATE fromVSSUpdate(String hl7Message) {
        if (hl7Message == null || hl7Message.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }

        LIS_SLIDE_UPDATE slideUpdate = new LIS_SLIDE_UPDATE();

        String[] lines = hl7Message.split("\\r?\\n");
        String obxLine = "";

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String segmentType = line.substring(0, 3);

            switch (segmentType) {
                case "MSH":
                    slideUpdate.msh = MSH_SLIDE_UPDATE.parseMSH(line);
                    break;
                case "PID":
                    slideUpdate.pid = parsePID(line);
                    break;
                case "SAC":
                    slideUpdate.sac = parseSAC(line);
                    break;
                case "ORC":
                    slideUpdate.orc = parseORCVSSUpdate(line);
                    break;
                case "OBR":
                    slideUpdate.obr = parseVSSOBR(line);
                    break;
                case "OBX":
                    obxLine = obxLine.isEmpty() ? line : obxLine + "\n" + line;
                    break;
            }
        }

        if (obxLine.isEmpty()) {
            throw new RuntimeException("Unable to parse SLIDE UPDATE from VSS to LIS");
        }

        slideUpdate.obx = OBX_SLIDE_UPDATE.parseOBX(obxLine);

        return slideUpdate;
    }
    public static LIS_SLIDE_UPDATE fromString(String hl7Message) {
        if (hl7Message == null || hl7Message.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }

        LIS_SLIDE_UPDATE slideUpdate = new LIS_SLIDE_UPDATE();

        String[] lines = hl7Message.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String segmentType = line.substring(0, 3);

            switch (segmentType) {
                case "MSH":
                    slideUpdate.msh = MSH_SLIDE_UPDATE.parseMSH(line);
                    break;
                case "PID":
                    slideUpdate.pid = parsePID(line);
                    break;
                case "SAC":
                    slideUpdate.sac = parseSAC(line);
                    break;
                case "ORC":
                    slideUpdate.orc = parseORC(line);
                    break;
                case "OBR":
                    slideUpdate.obr = parseOBR(line);
                    break;
            }
        }

        return slideUpdate;
    }
}
