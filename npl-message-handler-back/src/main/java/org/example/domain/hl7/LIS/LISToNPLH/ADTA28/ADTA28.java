
package org.example.domain.hl7.LIS.LISToNPLH.ADTA28;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class ADTA28 extends HL7Segment {

    MSH msh;
    PID pid;

    public static ADTA28 Default() {
        ADTA28 adta28 = new ADTA28();

        adta28.msh = MSH.Default();
        adta28.pid = PID.Default();

        return adta28;
    }
    
    public static ADTA28 FromMessage(Message message) {
        ADTA28 adta28 = new ADTA28();

        adta28.msh = MSH.FromMessageHeader(message.getHeader(), "ADT^A28");
        adta28.pid = PID.FromPatient(message.getPatient());

        return adta28;
    }

    @Override
    public String toString() {
        String caseUpdate = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n";

        return cleanMessage(caseUpdate);
    }

    public String getControlId() {
        return this.msh.getMessageControlID();
    }
}
