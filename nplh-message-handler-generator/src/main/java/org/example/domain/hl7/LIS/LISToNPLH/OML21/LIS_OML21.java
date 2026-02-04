package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

@Data
public class LIS_OML21 extends HL7Segment implements HL7Message {
    MSH_OML21 msh;
    PID_OML21 pid;
    PV1_OML21 pv1;
    SAC_OML21 sac;
    List<OSegment> oSegments = new ArrayList<>();

    public static LIS_OML21 OneSlide(String sampleId) {
        LIS_OML21 LISOML21 = new LIS_OML21();

        LISOML21.msh = MSH_OML21.Default();
        LISOML21.pid = PID_OML21.Default();
        LISOML21.pv1 = PV1_OML21.Default();
        LISOML21.sac = SAC_OML21.Default();

        LISOML21.oSegments.add(new OSegment(ORC_OML21.Default(sampleId),
                OBR_OML21.Default(sampleId, "A", "1", "1", "1"),
                OBX_OML21.Default(sampleId, "A", "1", "1", "1")));
        return LISOML21;
    }

    public static LIS_OML21 fromMessage(Message message) {
        LIS_OML21 LISOML21 = new LIS_OML21();

        LISOML21.msh = MSH_OML21.FromMessageHeader(message.getHeader(), "OML^O21");
        LISOML21.pid = PID_OML21.FromPatient(message.getPatient());
        LISOML21.pv1 = PV1_OML21.FromPhysician(message.getPhysician());
        LISOML21.sac = SAC_OML21.FromOrder(message.getOrder());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber ++;
            OBR_OML21 obr = OBR_OML21.FromMessage(slide, message, segmentNumber);
            ORC_OML21 orc = ORC_OML21.FromMessage(slide, message);
            OBX_OML21 obx = OBX_OML21.FromMessage(slide, message, segmentNumber);
            LISOML21.oSegments.add(new OSegment(orc, obr, obx));
        }

        return LISOML21;
    }

    public static LIS_OML21 fromSlide(Message message, Slide slide) {
        LIS_OML21 LISOML21 = new LIS_OML21();

        LISOML21.msh = MSH_OML21.FromMessageHeader(message.getHeader(), "OML^O21");
        LISOML21.pid = PID_OML21.FromPatient(message.getPatient());
        LISOML21.pv1 = PV1_OML21.FromPhysician(message.getPhysician());
        LISOML21.sac = SAC_OML21.FromOrder(message.getOrder());

        OBR_OML21 obr = OBR_OML21.FromMessage(slide, message, 1);
        ORC_OML21 orc = ORC_OML21.FromMessage(slide, message);
        OBX_OML21 obx = OBX_OML21.FromMessage(slide, message, 1);
        LISOML21.oSegments.add(new OSegment(orc, obr, obx));

        return LISOML21;
    }

    @Override
    public String toString() {
        String oml21 = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(pv1) + "\n" +
                nullSafe(sac) + "\n";

        String oSegmentsString = "";
        for (OSegment oSegment : oSegments) {
            oSegmentsString = oSegmentsString + nullSafe(oSegment.orc.toString()) + "\n" + nullSafe(oSegment.obr.toString()) + "\n" + nullSafe(oSegment.obx.toString()) + "\n";
        }
        return cleanMessage(oml21 + oSegmentsString);
    }

    @Override
    public String getControlId() {
        return this.msh.getMessageControlID();
    }

}
