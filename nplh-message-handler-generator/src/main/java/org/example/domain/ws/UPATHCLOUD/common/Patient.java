package org.example.domain.ws.UPATHCLOUD.common;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Patient extends WSSegment {
    @JacksonXmlProperty(localName = "contactInfo")
    private ContactInfo contactInfo;
    @JacksonXmlProperty(localName = "dateOfBirth")
    private String dateOfBirth;
    @JacksonXmlProperty(localName = "externalPatientId")
    private String externalPatientId;
    @JacksonXmlProperty(localName = "firstName")
    private String firstName;
    @JacksonXmlProperty(localName = "gender")
    private String gender;
    @JacksonXmlProperty(localName = "lastName")
    private String lastName;
    @JacksonXmlProperty(localName = "medicalHistory")
    private String medicalHistory;
    @JacksonXmlProperty(localName = "middleName")
    private String middleName;
    @JacksonXmlProperty(localName = "suffix")
    private String suffix;

    public static Patient fromPatient(org.example.domain.message.Patient entityPatient) {
        Patient patient = new Patient();

        patient.contactInfo = ContactInfo.fromContactInfo(entityPatient);
        patient.dateOfBirth = convertToXmlDateTime(entityPatient.getDateOfBirth());
        patient.externalPatientId = entityPatient.getCode();
        patient.firstName = entityPatient.getFirstName();
        patient.gender = entityPatient.getSex();
        patient.lastName = entityPatient.getLastName();
        patient.middleName = entityPatient.getMiddleName();
        patient.suffix = entityPatient.getSuffix();

        return patient;
    }

    public String toString(int indentationLevel) {

        String patient = addIndentation(indentationLevel) + "<patientDetails>\n";

        indentationLevel ++;

        patient += nullSafe(contactInfo, ContactInfo::new).toString(indentationLevel) + "\n"
                + addIndentation(indentationLevel) + "<dateOfBirth>" +  nullSafe(dateOfBirth) + "</dateOfBirth>\n"
                + addIndentation(indentationLevel) + "<externalPatientId>" +  nullSafe(externalPatientId) + "</externalPatientId>\n"
                + addIndentation(indentationLevel) + "<firstName>" +  nullSafe(firstName) + "</firstName>\n"
                + addIndentation(indentationLevel) + "<gender>" +  nullSafe(gender) + "</gender>\n"
                + addIndentation(indentationLevel) + "<lastName>" +  nullSafe(lastName) + "</lastName>\n"
                + addIndentation(indentationLevel) + "<middleName>" +  nullSafe(middleName) + "</middleName>\n"
                + addIndentation(indentationLevel) + "<suffix>" +  nullSafe(suffix) + "</suffix>\n";

        indentationLevel --;

        patient = addIndentation(indentationLevel) + "</patientDetails>";

        return patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient patient = (Patient) o;

        return Objects.equals(contactInfo, patient.contactInfo)
                && Objects.equals(dateOfBirth, patient.dateOfBirth)
                && Objects.equals(externalPatientId, patient.externalPatientId)
                && Objects.equals(firstName, patient.firstName)
                && Objects.equals(gender, patient.gender)
                && Objects.equals(lastName, patient.lastName)
                && Objects.equals(medicalHistory, patient.medicalHistory)
                && Objects.equals(middleName, patient.middleName)
                && Objects.equals(suffix, patient.suffix);
    }

}
