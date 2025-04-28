package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Patient extends WSSegment {

    private String dateofBirth;
    private String firstName;
    private String gender;
    private String lastName;
    private String middleName;
    private String patientId;
    private String suffix;
    private String zipCode;

    public static Patient FromPatient(org.example.domain.message.Patient entityPatient) {
        Patient patient = new Patient();

        patient.setDateofBirth(entityPatient.getDateOfBirth());
        patient.setFirstName(entityPatient.getFirstName());
        patient.setGender(entityPatient.getSex());
        patient.setLastName(entityPatient.getLastName());
        patient.setMiddleName(entityPatient.getMiddleName());
        patient.setPatientId(entityPatient.getCode());
        patient.setSuffix(entityPatient.getSuffix());
        patient.setZipCode(entityPatient.getZip());

        return patient;
    }

    private boolean isEmpty() {
        return Stream.of(
                        dateofBirth,
                        firstName,
                        gender,
                        lastName,
                        middleName,
                        patientId,
                        suffix,
                        zipCode
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Patient>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<DateofBirth>" +  nullSafe(dateofBirth) + "</DateofBirth>\n"
                    + addIndentation(indentationLevel) + "<FirstName>" +  nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<Gender>" +  nullSafe(gender) + "</Gender>\n"
                    + addIndentation(indentationLevel) + "<LastName>" +  nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" +  nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<PatientId>" +  nullSafe(patientId) + "</PatientId>\n"
                    + addIndentation(indentationLevel) + "<Suffix>" +  nullSafe(suffix) + "</Suffix>\n"
                    + addIndentation(indentationLevel) + "<ZipCode>" +  nullSafe(zipCode) + "</ZipCode>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Patient>";
        return specimen;
    }

}
