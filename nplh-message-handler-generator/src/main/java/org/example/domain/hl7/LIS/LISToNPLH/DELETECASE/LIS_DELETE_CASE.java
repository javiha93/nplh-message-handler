
package org.example.domain.hl7.LIS.LISToNPLH.DELETECASE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class LIS_DELETE_CASE extends HL7Segment {

    MSH msh;
    PID pid;
    OBR obr;
    ORC orc;

    public static LIS_DELETE_CASE Default(String sampleId) {
        LIS_DELETE_CASE deletecase = new LIS_DELETE_CASE();

        deletecase.msh = MSH.Default();
        deletecase.pid = PID.Default();
        deletecase.orc = ORC.Default(sampleId);
        deletecase.obr = OBR.Default(sampleId);

        return deletecase;
    }
    
    public static LIS_DELETE_CASE FromMessage(Message message) {
        LIS_DELETE_CASE deletecase = new LIS_DELETE_CASE();

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
