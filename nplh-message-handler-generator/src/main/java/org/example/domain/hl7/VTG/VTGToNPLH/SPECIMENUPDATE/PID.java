package org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Patient;

@Data
@NoArgsConstructor
public class PID extends org.example.domain.hl7.common.PID {

    public static PID Default() {
        return (PID) org.example.domain.hl7.common.PID.Default();
    }

    public static PID FromPatient(Patient patient) {
        return (PID) org.example.domain.hl7.common.PID.FromPatient(patient, new PID());
    }
    @Override
    public String toString() {
        return super.toString();
    }
}

