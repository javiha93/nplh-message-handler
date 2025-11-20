package org.example.domain.hl7.VTG.NPLHToVTG.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.PID;
import org.example.domain.message.Patient;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PID_OML21 extends PID {

    public static PID_OML21 Default() {
        return (PID_OML21) PID.Default(new PID_OML21());
    }

    public static PID_OML21 FromPatient(Patient patient) {
        return (PID_OML21) PID.FromPatient(patient, new PID_OML21());
    }

    protected static PID_OML21 parsePID(String line) {
        return (PID_OML21) PID.parsePID(line, new PID_OML21());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PID_OML21 pid = (PID_OML21) o;

        return Objects.equals(extPatientID, pid.extPatientID) &&
                Objects.equals(lastName, pid.lastName) &&
                Objects.equals(firstName, pid.firstName) &&
                Objects.equals(middleName, pid.middleName) &&
                Objects.equals(suffix, pid.suffix) &&
                Objects.equals(secondSurname, pid.secondSurname) &&
                Objects.equals(dateOfBirth, pid.dateOfBirth) &&
                Objects.equals(sex, pid.sex);
    }

    @Override
    public String toString() {
        String value = "PID|||" +
                nullSafe(extPatientID) + "||" +                                                                                              // 3
                nullSafe(lastName) + "^" + nullSafe(firstName) + "^" + nullSafe(middleName) + "^" + nullSafe(suffix) + "|" +                 // 5
                nullSafe(secondSurname) + "|" +                                                                                              // 6
                nullSafe(dateOfBirth) + "|" +                                                                                                // 7
                nullSafe(sex) + "|";                                                                                                       // 8                                                                                                     // 14
        return cleanSegment(value);
    }
}

