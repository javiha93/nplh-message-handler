package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Patient extends WSSegment {
    @JacksonXmlProperty(localName = "MedicalRecordNumber")
    private String medicalRecordNumber;
    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "MiddleName")
    private String middleName;
    @JacksonXmlProperty(localName = "LastName")
    private String lastName;
    @JacksonXmlProperty(localName = "DateOfBirth")
    private String dateOfBirth;
    @JacksonXmlProperty(localName = "Gender")
    private String gender;
    @JacksonXmlProperty(localName = "Prefix")
    private String prefix;
    @JacksonXmlProperty(localName = "Suffix")
    private String suffix;

    public static Patient fromPatient(org.example.domain.message.Patient entityPatient) {
        if (entityPatient.isEmpty()) {
            return null;
        }

        Patient patient = new Patient();

        patient.setMedicalRecordNumber(entityPatient.getCode());
        patient.setFirstName(entityPatient.getFirstName());
        patient.setLastName(entityPatient.getLastName());
        patient.setMiddleName(entityPatient.getMiddleName());
        patient.setDateOfBirth(convertToXmlDateTime(entityPatient.getDateOfBirth()));
        String sex = entityPatient.getSex();
        if (sex.equals("M")) {
            patient.setGender("Male");
        } else if (sex.equals("F")) {
            patient.setGender("Female");
        } else {
            patient.setGender("Unknown");
        }

        patient.setSuffix(entityPatient.getSuffix());
        patient.setPrefix(entityPatient.getPrefix());

        return patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        return Objects.equals(medicalRecordNumber, patient.medicalRecordNumber)
                && Objects.equals(firstName, patient.firstName)
                && Objects.equals(lastName, patient.lastName)
                && Objects.equals(middleName, patient.middleName)
                && Objects.equals(dateOfBirth, patient.dateOfBirth)
                && Objects.equals(gender, patient.gender)
                && Objects.equals(suffix, patient.suffix)
                && Objects.equals(prefix, patient.prefix);
    }

    private boolean isEmpty() {
        return Stream.of(
                        firstName,
                        medicalRecordNumber,
                        lastName,
                        middleName,
                        dateOfBirth
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String pathologist = addIndentation(indentationLevel) + "<Patient>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            pathologist += addIndentation(indentationLevel) + "<MedicalRecordNumber>" +  nullSafe(medicalRecordNumber) + "</MedicalRecordNumber>\n"
                    + addIndentation(indentationLevel) + "<FirstName>" +  nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" +  nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<LastName>" +  nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<DateOfBirth>" +  nullSafe(dateOfBirth) + "</DateOfBirth>\n"
                    + addIndentation(indentationLevel) + "<Gender>" +  nullSafe(gender) + "</Gender>\n"
                    + addIndentation(indentationLevel) + "<Prefix>" +  nullSafe(prefix) + "</Prefix>\n"
                    + addIndentation(indentationLevel) + "<Suffix>" +  nullSafe(suffix) + "</Suffix>\n";

            indentationLevel --;
        }

        pathologist += addIndentation(indentationLevel) + "</Patient>";
        return pathologist;
    }

}
