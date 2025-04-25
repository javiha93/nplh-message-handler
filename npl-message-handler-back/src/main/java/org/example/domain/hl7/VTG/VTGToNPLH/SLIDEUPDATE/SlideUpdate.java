package org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;


public class SlideUpdate extends HL7Segment {
    MSH msh;
    PID pid;
    SAC sac;
    ORC orc;
    OBR obr;

    public static SlideUpdate FromMessage(Message message, Slide slide, String slideStatus) {
        SlideUpdate slideUpdate = new SlideUpdate();

        slideUpdate.msh = MSH.FromMessageHeader(message.getHeader());
        slideUpdate.pid = PID.FromPatient(message.getPatient());
        slideUpdate.sac = SAC.FromOrder(message.getOrder());
        slideUpdate.orc = ORC.FromMessage(slide, message, slideStatus);
        slideUpdate.obr = OBR.FromMessage(slide, message);

        return slideUpdate;
    }

    @Override
    public String toString() {
        String slideUpdate = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(sac) + "\n" +
                nullSafe(orc) + "\n" +
                nullSafe(obr) + "\n";

        return cleanMessage(slideUpdate );
    }
}
