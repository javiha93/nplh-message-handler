
package org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Patient;

public class PID extends org.example.domain.hl7.common.PID {

    public static PID Default() {
        return (PID) org.example.domain.hl7.common.PID.Default(new PID());
    }

    public static PID FromPatient(Patient patient) {
        return (PID) org.example.domain.hl7.common.PID.FromPatient(patient, new PID());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
