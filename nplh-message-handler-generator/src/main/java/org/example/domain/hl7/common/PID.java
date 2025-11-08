package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.hl7.VTG.NPLHToVTG.PID_OML21;
import org.example.domain.message.Patient;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PID extends HL7Segment {

    @HL7Position(position = 3, subPosition = 1)
    protected String extPatientID;

    @HL7Position(position = 5, subPosition = 1)
    protected String lastName;

    @HL7Position(position = 5, subPosition = 2)
    protected String firstName;

    @HL7Position(position = 5, subPosition = 3)
    protected String middleName;

    @HL7Position(position = 5, subPosition = 4)
    protected String suffix;

    @HL7Position(position = 6)
    protected String secondSurname;

    @HL7Position(position = 7)
    protected String dateOfBirth;

    @HL7Position(position = 8)
    protected String sex;

    @HL7Position(position = 11, subPosition = 1)
    protected String address1;

    @HL7Position(position = 11, subPosition = 3)
    protected String city;

    @HL7Position(position = 11, subPosition = 4)
    protected String state;

    @HL7Position(position = 11, subPosition = 5)
    protected String zip;

    @HL7Position(position = 11, subPosition = 6)
    protected String country;

    @HL7Position(position = 13, subPosition = 1)
    protected String homeTel;

    @HL7Position(position = 13, subPosition = 4)
    protected String email;

    @HL7Position(position = 13, subPosition = 12)
    protected String mobileTel;

    @HL7Position(position = 14, subPosition = 1)
    protected String workTel;

    public static PID Default() {
        PID pid = new PID();

        pid.extPatientID = "MN-10000000";
        pid.lastName = "LastName";
        pid.firstName = "FirstName";
        pid.middleName = "MiddleInitial";
        pid.suffix = "Sr.";
        pid.dateOfBirth = "19800101";
        pid.sex = "M";

        return pid;
    }

    public static PID Default(PID pid) {
        pid.extPatientID = "MN-10000000";
        pid.lastName = "LastName";
        pid.firstName = "FirstName";
        pid.middleName = "MiddleInitial";
        pid.suffix = "Sr.";
        pid.dateOfBirth = "19800101";
        pid.sex = "M";

        return pid;
    }

    public static PID FromPatient(Patient patient, PID pid) {
        pid.extPatientID = patient.getCode();
        pid.lastName = patient.getLastName();
        pid.firstName = patient.getFirstName();
        pid.middleName = patient.getMiddleName();
        pid.suffix = patient.getSuffix();
        pid.dateOfBirth = patient.getDateOfBirth();
        pid.sex = patient.getSex();
        pid.zip = patient.getZip();
        pid.secondSurname = patient.getSecondSurname();

        return pid;
    }

    @Override
    public String toString() {
        String value = "PID|||" +
                nullSafe(extPatientID) + "||" +                                                                                              // 3
                nullSafe(lastName) + "^" + nullSafe(firstName) + "^" + nullSafe(middleName) + "^" + nullSafe(suffix) + "|" +                 // 5
                nullSafe(secondSurname) + "|" +                                                                                              // 6
                nullSafe(dateOfBirth) + "|" +                                                                                                // 7
                nullSafe(sex) + "|||" +                                                                                                      // 8
                nullSafe(address1) + "^^" + nullSafe(city) + "^" + nullSafe(state) + "^" + nullSafe(zip) + "^" + nullSafe(country) + "||" +  // 11
                nullSafe(homeTel) + "^^^" + nullSafe(email) + "^^^^^^^^" + nullSafe(mobileTel) + "|" +                                       // 13
                nullSafe(workTel) + "|";                                                                                                     // 14
        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PID pid = (PID) o;

        return Objects.equals(extPatientID, pid.extPatientID) &&
                Objects.equals(lastName, pid.lastName) &&
                Objects.equals(firstName, pid.firstName) &&
                Objects.equals(middleName, pid.middleName) &&
                Objects.equals(suffix, pid.suffix) &&
                Objects.equals(secondSurname, pid.secondSurname) &&
                Objects.equals(dateOfBirth, pid.dateOfBirth) &&
                Objects.equals(sex, pid.sex) &&
                Objects.equals(address1, pid.address1) &&
                Objects.equals(city, pid.city) &&
                Objects.equals(state, pid.state) &&
                Objects.equals(zip, pid.zip) &&
                Objects.equals(country, pid.country) &&
                Objects.equals(homeTel, pid.homeTel) &&
                Objects.equals(email, pid.email) &&
                Objects.equals(mobileTel, pid.mobileTel) &&
                Objects.equals(workTel, pid.workTel);
    }

    protected static PID parsePID(String line, PID pid) {
        String[] fields = line.split("\\|");

        // Campo 3 (position 3) - Patient ID
        if (fields.length > 3) {
            pid.setExtPatientID(getFieldValue(fields, 3));
        }

        // Campo 5 (position 5) - Patient Name (LastName^FirstName^MiddleInitial^Suffix)
        if (fields.length > 5) {
            String[] name = fields[5].split("\\^");
            if (name.length > 0) pid.setLastName(name[0]);
            if (name.length > 1) pid.setFirstName(name[1]);
            if (name.length > 2) pid.setMiddleName(name[2]);
            if (name.length > 3) pid.setSuffix(name[3]);
        }

        // Campo 7 (position 7) - Date of Birth
        if (fields.length > 7) {
            pid.setDateOfBirth(getFieldValue(fields, 7));
        }

        // Campo 8 (position 8) - Sex
        if (fields.length > 8) {
            pid.setSex(getFieldValue(fields, 8));
        }

        return pid;
    }
}

