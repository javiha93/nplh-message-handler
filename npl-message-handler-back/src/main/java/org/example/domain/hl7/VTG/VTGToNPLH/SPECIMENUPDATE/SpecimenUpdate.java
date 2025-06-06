package org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Specimen;


public class SpecimenUpdate extends HL7Segment {
    MSH msh;
    PID pid;
    SAC sac;
    ORC orc;
    OBR obr;

    public static SpecimenUpdate FromMessage(Message message, Specimen specimen, String specimenStatus) {
        SpecimenUpdate specimenUpdate = new SpecimenUpdate();
        Order order = message.getOrder();

        specimenUpdate.msh = MSH.FromMessageHeader(message.getHeader());
        specimenUpdate.pid = PID.FromPatient(message.getPatient());
        specimenUpdate.sac = SAC.FromOrder(order);
        specimenUpdate.orc = ORC.FromMessage(specimen, order.getSampleId(), specimenStatus);
        specimenUpdate.obr = OBR.FromMessage(specimen, message);

        return specimenUpdate;
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
