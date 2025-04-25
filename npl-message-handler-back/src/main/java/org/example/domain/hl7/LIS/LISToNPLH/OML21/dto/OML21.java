package org.example.domain.hl7.LIS.LISToNPLH.OML21.dto;

import lombok.Data;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

@Data
public class OML21 extends HL7Segment {
    MSH msh;
    PID pid;
    PV1 pv1;
    SAC sac;
    List<OSegment> oSegments = new ArrayList<>();

    public static OML21 OneSlide(String sampleId) {
        OML21 oml21 = new OML21();

        oml21.msh = MSH.Default();
        oml21.pid = PID.Default();
        oml21.pv1 = PV1.Default();
        oml21.sac = SAC.Default();

        oml21.oSegments.add(new OSegment(ORC.Default(sampleId),
                OBR.Default(sampleId, "A", "1", "1", "1"),
                OBX.Default(sampleId, "A", "1", "1", "1")));
        return oml21;
    }

    public static OML21 FromMessage(Message message) {
        OML21 oml21 = new OML21();

        oml21.msh = MSH.FromMessageHeader(message.getHeader(), "OML^O21");
        oml21.pid = PID.FromPatient(message.getPatient());
        oml21.pv1 = PV1.FromPhysician(message.getPhysician());
        oml21.sac = SAC.FromOrder(message.getOrder());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber ++;
            OBR obr = OBR.FromMessage(slide, message, segmentNumber);
            ORC orc = ORC.FromMessage(slide, message);
            OBX obx = OBX.FromMessage(slide, message, segmentNumber);
            oml21.oSegments.add(new OSegment(orc, obr, obx));
        }

        return oml21;
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

}
