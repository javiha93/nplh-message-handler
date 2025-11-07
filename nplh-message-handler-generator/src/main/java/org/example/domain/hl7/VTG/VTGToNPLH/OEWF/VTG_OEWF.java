
package org.example.domain.hl7.VTG.VTGToNPLH.OEWF;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

public class VTG_OEWF extends HL7Segment {
    MSH msh;
    PID pid;
    PV1 pv1;
    List<OSegment> oSegments = new ArrayList<>();

    public static VTG_OEWF OneSlide(String sampleId) {
        VTG_OEWF oml21 = new VTG_OEWF();

        oml21.msh = MSH.Default();
        oml21.pid = PID.Default();
        oml21.pv1 = PV1.Default();

        oml21.oSegments.add(new OSegment(ORC.Default(sampleId),
                OBR.Default(sampleId, "A", "1", "1", "1"),
                OBX.Default(sampleId, "A", "1", "1", "1")));
        return oml21;
    }
    
    public static VTG_OEWF FromMessage(Message message) {
        VTG_OEWF oewf = new VTG_OEWF();

        oewf.msh = MSH.FromMessageHeader(message.getHeader(), "OML^O21");
        oewf.pid = PID.FromPatient(message.getPatient());
        oewf.pv1 = PV1.FromPhysician(message.getPhysician());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber++;
            OBR obr = OBR.FromMessage(slide, message, segmentNumber);
            ORC orc = ORC.FromMessage(slide, message);
            OBX obx = OBX.FromMessage(slide, message, segmentNumber);
            oewf.oSegments.add(new OSegment(orc, obr, obx));
        }

        return oewf;
    }

    @Override
    public String toString() {
        String oml21 = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(pv1) + "\n";

        String oSegmentsString = "";
        for (OSegment oSegment : oSegments) {
            oSegmentsString = oSegmentsString + nullSafe(oSegment.orc.toString()) + "\n" +
                    nullSafe(oSegment.obr.toString()) + "\n" +
                    nullSafe(oSegment.obx.toString()) + "\n";
        }
        return cleanMessage(oml21 + oSegmentsString);
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
