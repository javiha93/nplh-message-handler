package org.example.domain.hl7.VTG.VTGToNPLH.ADDITION;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

public class Addition extends HL7Segment {

    MSH msh;
    PID pid;
    List<OSegment> oSegments = new ArrayList<>();

    public static Addition FromMessage(Message message) {
        Addition addition = new Addition();

        addition.msh = MSH.FromMessageHeader(message.getHeader(), "OUL^R21");
        addition.pid = PID.FromPatient(message.getPatient());

        int segmentNumber = 0;
        for (Slide slide : message.getAllSlides()) {
            segmentNumber ++;
            OBR obr = OBR.FromMessage(slide, message, segmentNumber);
            ORC orc = ORC.FromMessage(slide, message);
            addition.oSegments.add(new OSegment(orc, obr));
        }
        for (Block block : message.getAllBlocks()) {
            if (block.hasSlide()) {
                continue;
            }
            segmentNumber ++;
            OBR obr = OBR.FromMessage(block, message, segmentNumber);
            ORC orc = ORC.FromMessage(block, message);
            addition.oSegments.add(new OSegment(orc, obr));
        }

        return addition;
    }

    @Override
    public String toString() {
        String addition = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n";

        String oSegmentsString = "";
        for (OSegment oSegment : oSegments) {
            oSegmentsString = oSegmentsString + nullSafe(oSegment.orc.toString()) + "\n" +
                    nullSafe(oSegment.obr.toString()) + "\n";
        }
        return cleanMessage(addition + oSegmentsString);
    }
}
