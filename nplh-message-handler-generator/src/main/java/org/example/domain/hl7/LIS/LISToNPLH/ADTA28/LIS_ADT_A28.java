
package org.example.domain.hl7.LIS.LISToNPLH.ADTA28;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class LIS_ADT_A28 extends HL7Segment {

    MSH msh;
    PID pid;

    public static LIS_ADT_A28 Default() {
        LIS_ADT_A28 adta28 = new LIS_ADT_A28();

        adta28.msh = MSH.Default();
        adta28.pid = PID.Default();

        return adta28;
    }
    
    public static LIS_ADT_A28 FromMessage(Message message) {
        LIS_ADT_A28 adta28 = new LIS_ADT_A28();

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
