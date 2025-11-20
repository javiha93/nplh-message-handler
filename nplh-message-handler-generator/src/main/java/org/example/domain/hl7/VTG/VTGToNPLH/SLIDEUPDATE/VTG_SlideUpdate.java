package org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE;

import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

import java.time.LocalDateTime;


public class VTG_SlideUpdate extends HL7Segment implements HL7Message {
    MSH_SlideUpdate msh;
    PID pid;
    SAC sac;
    ORC orc;
    OBR obr;

    //TODO enum slideStatus
    public static VTG_SlideUpdate fromMessage(Message message, Slide slide, String slideStatus) {
        VTG_SlideUpdate slideUpdate = new VTG_SlideUpdate();

        slideUpdate.msh = MSH_SlideUpdate.FromMessageHeaderVTGSender(message.getHeader());
        slideUpdate.pid = PID.FromPatient(message.getPatient());
        slideUpdate.sac = SAC.FromOrder(message.getOrder());
        slideUpdate.orc = ORC.FromMessage(slide, message, slideStatus);
        slideUpdate.obr = OBR.FromMessage(slide, message);

        //updateSlideStatus(slide, slideStatus);

        return slideUpdate;
    }

    private static void updateSlideStatus(Slide slide, String slideStatus) {
        if (slideStatus.equals("PRINTED")) {
            slide.setLabelPrinted(LocalDateTime.now().toString());
        }
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

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
