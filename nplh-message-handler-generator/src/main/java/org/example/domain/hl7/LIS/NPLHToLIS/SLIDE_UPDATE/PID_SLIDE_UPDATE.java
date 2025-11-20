package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import org.example.domain.hl7.VTG.NPLHToVTG.OML21.PID_OML21;
import org.example.domain.hl7.common.PID;
import org.example.domain.message.Patient;

@Data
public class PID_SLIDE_UPDATE extends PID {

    public static PID_SLIDE_UPDATE FromPatient(Patient patient) {
        PID_SLIDE_UPDATE pid = new PID_SLIDE_UPDATE();

        pid.extPatientID = patient.getCode();
        pid.lastName = patient.getLastName();
        pid.firstName = patient.getFirstName();
        pid.middleName = patient.getMiddleName();
        pid.suffix = patient.getSuffix();
        pid.dateOfBirth = patient.getDateOfBirth();
        pid.sex = patient.getSex();

        return pid;
    }
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    protected static PID_SLIDE_UPDATE parsePID(String line) {
        return (PID_SLIDE_UPDATE) PID.parsePID(line, new PID_SLIDE_UPDATE());
    }
}
