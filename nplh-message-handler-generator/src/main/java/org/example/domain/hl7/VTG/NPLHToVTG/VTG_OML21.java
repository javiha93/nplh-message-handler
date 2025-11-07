package org.example.domain.hl7.VTG.NPLHToVTG;

import lombok.Data;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

@Data
public class VTG_OML21 extends HL7Segment {
    MSH_OML21 msh;
    PID_OML21 pid;
    PV1_OML21 pv1;
    List<OSegment> oSegments = new ArrayList<>();

    public static VTG_OML21 FromMessage(Message message) {
        VTG_OML21 oml21 = new VTG_OML21();

        oml21.msh = MSH_OML21.FromMessageHeader(message.getHeader(), "OML^O21");
        oml21.pid = PID_OML21.FromPatient(message.getPatient());
        oml21.pv1 = PV1_OML21.FromPhysician(message.getPhysician());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber ++;
            OBR_OML21 obr = OBR_OML21.FromMessage(slide, message, segmentNumber);
            ORC_OML21 orc = ORC_OML21.FromMessage(slide, message);
            oml21.oSegments.add(new OSegment(orc, obr));
        }

        return oml21;
    }

    @Override
    public String toString() {
        String oml21 = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(pv1) + "\n";

        String oSegmentsString = "";
        for (OSegment oSegment : oSegments) {
            oSegmentsString = oSegmentsString + nullSafe(oSegment.orc.toString()) + "\n" + nullSafe(oSegment.obr.toString()) + "\n";
        }
        return cleanMessage(oml21 + oSegmentsString);
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
