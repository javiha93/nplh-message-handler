
package org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class LIS_CASEUPDATE extends HL7Segment {

    MSH msh;
    PID pid;
    OBR obr;
    ORC orc;

    public static LIS_CASEUPDATE Default(String sampleId, String status) {
        LIS_CASEUPDATE caseupdate = new LIS_CASEUPDATE();

        caseupdate.msh = MSH.Default();
        caseupdate.pid = PID.Default();
        caseupdate.orc = ORC.Default(sampleId, status);
        caseupdate.obr = OBR.Default(sampleId);

        return caseupdate;
    }
    
    public static LIS_CASEUPDATE FromMessage(Message message, String status) {
        LIS_CASEUPDATE caseupdate = new LIS_CASEUPDATE();

        caseupdate.msh = MSH.FromMessageHeader(message.getHeader(), "OUL^R21");
        caseupdate.pid = PID.FromPatient(message.getPatient());
        caseupdate.orc = ORC.FromMessage(message.getOrder(), status);
        caseupdate.obr = OBR.FromMessage(message.getOrder());

        return caseupdate;
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
