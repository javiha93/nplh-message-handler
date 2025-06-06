
package org.example.domain.hl7.LIS.LISToNPLH.DELETECASE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class DELETECASE extends HL7Segment {

    MSH msh;
    PID pid;
    OBR obr;
    ORC orc;

    public static DELETECASE Default(String sampleId) {
        DELETECASE deletecase = new DELETECASE();

        deletecase.msh = MSH.Default();
        deletecase.pid = PID.Default();
        deletecase.orc = ORC.Default(sampleId);
        deletecase.obr = OBR.Default(sampleId);

        return deletecase;
    }
    
    public static DELETECASE FromMessage(Message message) {
        DELETECASE deletecase = new DELETECASE();

        deletecase.msh = MSH.FromMessageHeader(message.getHeader(), "OUL^R21");
        deletecase.pid = PID.FromPatient(message.getPatient());
        deletecase.orc = ORC.FromMessage(message.getOrder());
        deletecase.obr = OBR.FromMessage(message.getOrder());

        return deletecase;
    }

    @Override
    public String toString() {
        String caseUpdate = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(orc) + "\n" +
                nullSafe(obr) + "\n";

        return cleanMessage(caseUpdate);
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
