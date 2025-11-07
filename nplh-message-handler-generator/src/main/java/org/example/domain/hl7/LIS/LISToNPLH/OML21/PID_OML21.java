package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.PID;
import org.example.domain.message.Patient;

@Data
@NoArgsConstructor
public class PID_OML21 extends org.example.domain.hl7.common.PID {

    public static PID_OML21 Default() {
        return (PID_OML21) PID.Default(new PID_OML21());
    }

    public static PID_OML21 FromPatient(Patient patient) {
        return (PID_OML21) PID.FromPatient(patient, new PID_OML21());
    }
    @Override
    public String toString() {
        return super.toString();
    }
}

