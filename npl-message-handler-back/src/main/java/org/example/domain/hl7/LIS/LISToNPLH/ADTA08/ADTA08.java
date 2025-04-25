
package org.example.domain.hl7.LIS.LISToNPLH.ADTA08;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;

public class ADTA08 extends HL7Segment {

    MSH msh;
    PID pid;
    PV1 pv1;
    OBX obx;

    public static ADTA08 Default(String sampleId) {
        ADTA08 adta08 = new ADTA08();

        adta08.msh = MSH.Default();
        adta08.pid = PID.Default();
        adta08.pv1 = PV1.Default();
        adta08.obx = OBX.Default(sampleId);

        return adta08;
    }
    
    public static ADTA08 FromMessage(Message message) {
        ADTA08 adta08 = new ADTA08();

        adta08.msh = MSH.FromMessageHeader(message.getHeader(), "ADT^A08");
        adta08.pid = PID.FromPatient(message.getPatient());
        adta08.pv1 = PV1.FromPhysician(message.getPhysician());
        adta08.obx = OBX.FromOrder(message.getOrder());

        return adta08;
    }

    @Override
    public String toString() {
        String caseUpdate = nullSafe(msh) + "\n" +
                nullSafe(pid) + "\n" +
                nullSafe(pv1) + "\n" +
                nullSafe(obx) + "\n";

        return cleanMessage(caseUpdate);
    }
}
