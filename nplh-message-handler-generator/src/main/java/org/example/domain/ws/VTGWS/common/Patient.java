package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Patient extends WSSegment {

    @JacksonXmlProperty(localName = "DateofBirth")
    private String dateofBirth;
    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "Gender")
    private String gender;
    @JacksonXmlProperty(localName = "LastName")
    private String lastName;
    @JacksonXmlProperty(localName = "MiddleName")
    private String middleName;
    @JacksonXmlProperty(localName = "PatientId")
    private String patientId;
    @JacksonXmlProperty(localName = "Suffix")
    private String suffix;
    @JacksonXmlProperty(localName = "ZipCode")
    private String zipCode;

    public static Patient FromPatient(org.example.domain.message.Patient entityPatient) {
        Patient patient = new Patient();

        patient.setDateofBirth(convertToXmlDateTime(entityPatient.getDateOfBirth()));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        return Objects.equals(dateofBirth, patient.dateofBirth)
                && Objects.equals(firstName, patient.firstName)
                && Objects.equals(gender, patient.gender)
                && Objects.equals(lastName, patient.lastName)
                && Objects.equals(middleName, patient.middleName)
                && Objects.equals(patientId, patient.patientId)
                && Objects.equals(suffix, patient.suffix)
                && Objects.equals(zipCode, patient.zipCode);
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
