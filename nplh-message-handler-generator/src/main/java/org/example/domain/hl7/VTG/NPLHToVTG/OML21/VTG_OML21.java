package org.example.domain.hl7.VTG.NPLHToVTG.OML21;

import lombok.Data;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.domain.hl7.VTG.NPLHToVTG.OML21.MSH_OML21.parseMSH;
import static org.example.domain.hl7.VTG.NPLHToVTG.OML21.OBR_OML21.parseOBR;
import static org.example.domain.hl7.VTG.NPLHToVTG.OML21.ORC_OML21.parseORC;
import static org.example.domain.hl7.VTG.NPLHToVTG.OML21.PID_OML21.parsePID;
import static org.example.domain.hl7.VTG.NPLHToVTG.OML21.PV1_OML21.parsePV1;

@Data
public class VTG_OML21 extends HL7Segment implements HL7Message {
    MSH_OML21 msh;
    PID_OML21 pid;
    PV1_OML21 pv1;
    List<OSegment> oSegments = new ArrayList<>();

    public static VTG_OML21 fromMessage(Message message) {
        VTG_OML21 oml21 = new VTG_OML21();

        oml21.msh = MSH_OML21.FromMessageHeader(message.getHeader(), "OML^O21");
        oml21.pid = PID_OML21.FromPatient(message.getPatient());
        oml21.pv1 = PV1_OML21.FromPhysician(message.getPhysician());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber ++;
            OBR_OML21 obr = OBR_OML21.fromMessage(slide, message, segmentNumber);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VTG_OML21 vtg_oml21 = (VTG_OML21) o;
        return Objects.equals(msh, vtg_oml21.msh) &&
                Objects.equals(pid, vtg_oml21.pid) &&
                Objects.equals(pv1, vtg_oml21.pv1) &&
                Objects.equals(oSegments, vtg_oml21.oSegments);
    }

    public static VTG_OML21 fromString(String hl7Message) {
        if (hl7Message == null || hl7Message.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }

        VTG_OML21 oml21 = new VTG_OML21();

        String[] lines = hl7Message.split("\\r?\\n");
        
        ORC_OML21 currentOrc = null;
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            
            String segmentType = line.substring(0, 3);
            
            switch (segmentType) {
                case "MSH":
                    oml21.msh = parseMSH(line);
                    break;
                case "PID":
                    oml21.pid = parsePID(line);
                    break;
                case "PV1":
                    oml21.pv1 = parsePV1(line);
                    break;
                case "ORC":
                    currentOrc = parseORC(line);
                    break;
                case "OBR":
                    if (currentOrc != null) {
                        OBR_OML21 obr = parseOBR(line);
                        oml21.oSegments.add(new OSegment(currentOrc, obr));
                        currentOrc = null;
                    }
                    break;
            }
        }
        
        return oml21;
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
