package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Patient;

@Data
@NoArgsConstructor
public class PID extends HL7Segment {

    @HL7Position(position = 3, subPosition = 1)
    private String extPatientID;

    @HL7Position(position = 5, subPosition = 1)
    private String lastName;

    @HL7Position(position = 5, subPosition = 2)
    private String firstName;

    @HL7Position(position = 5, subPosition = 3)
    private String middleName;

    @HL7Position(position = 5, subPosition = 4)
    private String suffix;

    @HL7Position(position = 6)
    private String secondSurname;

    @HL7Position(position = 7)
    private String dateOfBirth;

    @HL7Position(position = 8)
    private String sex;

    @HL7Position(position = 11, subPosition = 1)
    private String address1;

    @HL7Position(position = 11, subPosition = 3)
    private String city;

    @HL7Position(position = 11, subPosition = 4)
    private String state;

    @HL7Position(position = 11, subPosition = 5)
    private String zip;

    @HL7Position(position = 11, subPosition = 6)
    private String country;

    @HL7Position(position = 13, subPosition = 1)
    private String homeTel;

    @HL7Position(position = 13, subPosition = 4)
    private String email;

    @HL7Position(position = 13, subPosition = 12)
    private String mobileTel;

    @HL7Position(position = 14, subPosition = 1)
    private String workTel;

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
}

