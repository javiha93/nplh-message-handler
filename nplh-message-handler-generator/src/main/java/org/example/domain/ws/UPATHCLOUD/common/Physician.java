package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Physician extends WSSegment {
    @JacksonXmlProperty(localName = "contactInfo")
    private ContactInfo contactInfo;
    @JacksonXmlProperty(localName = "externalPhysicianId")
    private String externalPhysicianId;
    @JacksonXmlProperty(localName = "firstName")
    private String firstName;
    @JacksonXmlProperty(localName = "lastName")
    private String lastName;
    @JacksonXmlProperty(localName = "middleName")
    private String middleName;
    @JacksonXmlProperty(localName = "suffix")
    private String suffix;

    public static Physician fromPhysician(org.example.domain.message.professional.Physician entityPhysician) {
        Physician physician = new Physician();

        physician.contactInfo = ContactInfo.fromContactInfo(entityPhysician);
        physician.externalPhysicianId = entityPhysician.getCode();
        physician.firstName = entityPhysician.getFirstName();
        physician.lastName = entityPhysician.getLastName();
        physician.middleName = entityPhysician.getMiddleName();
        physician.suffix = entityPhysician.getSuffix();

        return physician;
    }

    public String toString(int indentationLevel) {

        String physician = addIndentation(indentationLevel) + "<referringPhysician>\n";

        indentationLevel ++;

        physician += nullSafe(contactInfo, ContactInfo::new).toString(indentationLevel) + "\n"
                + addIndentation(indentationLevel) + "<externalPhysicianId>" +  nullSafe(externalPhysicianId) + "</externalPhysicianId>\n"
                + addIndentation(indentationLevel) + "<firstName>" +  nullSafe(firstName) + "</firstName>\n"
                + addIndentation(indentationLevel) + "<lastName>" +  nullSafe(lastName) + "</lastName>\n"
                + addIndentation(indentationLevel) + "<middleName>" +  nullSafe(middleName) + "</middleName>\n"
                + addIndentation(indentationLevel) + "<suffix>" +  nullSafe(suffix) + "</suffix>\n";

        indentationLevel --;

        physician += addIndentation(indentationLevel) + "</referringPhysician>";

        return physician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Physician physician = (Physician) o;

        return Objects.equals(contactInfo, physician.contactInfo)
                && Objects.equals(externalPhysicianId, physician.externalPhysicianId)
                && Objects.equals(firstName, physician.firstName)
                && Objects.equals(lastName, physician.lastName)
                && Objects.equals(middleName, physician.middleName)
                && Objects.equals(suffix, physician.suffix);
    }

}
