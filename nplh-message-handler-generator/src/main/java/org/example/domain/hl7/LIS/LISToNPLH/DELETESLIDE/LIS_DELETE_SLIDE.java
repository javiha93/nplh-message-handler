
package org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

public class LIS_DELETE_SLIDE extends HL7Segment {
    MSH msh;
    PID pid;
    OBR obr;
    ORC orc;

    public static LIS_DELETE_SLIDE Default(String sampleId, String slideId) {
        LIS_DELETE_SLIDE deleteSlide = new LIS_DELETE_SLIDE();

        deleteSlide.msh = MSH.Default();
        deleteSlide.pid = PID.Default();
        deleteSlide.orc = ORC.Default(sampleId, slideId);
        deleteSlide.obr = OBR.Default(slideId);

        return deleteSlide;
    }
    
    public static LIS_DELETE_SLIDE FromMessage(Message message, Slide slide) {
        LIS_DELETE_SLIDE deleteSlide = new LIS_DELETE_SLIDE();

        deleteSlide.msh = MSH.FromMessageHeader(message.getHeader(), "OUL^R21");
        deleteSlide.pid = PID.FromPatient(message.getPatient());
        deleteSlide.orc = ORC.FromMessage(slide, message);
        deleteSlide.obr = OBR.FromMessage(slide, message);

        return deleteSlide;
    }

    @Override
    public String toString() {
        String deleteSlide = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(orc) + "\n" +
                nullSafe(obr) + "\n";

        return cleanMessage(deleteSlide);
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
