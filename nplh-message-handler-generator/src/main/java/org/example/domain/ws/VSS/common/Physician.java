package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.VTGWS.common.Pathologist;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Physician extends WSSegment {
    @JacksonXmlProperty(localName = "Code")
    private String code;
    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "LastName")
    private String lastName;
    @JacksonXmlProperty(localName = "MiddleName")
    private String middleName;
    @JacksonXmlProperty(localName = "Prefix")
    private String prefix;
    @JacksonXmlProperty(localName = "Suffix")
    private String suffix;

    public static Physician FromPhysician(org.example.domain.message.professional.Physician entityPhysician) {
        if (entityPhysician.isEmpty()) {
            return null;
        }

        Physician physician = new Physician();

        physician.setCode(entityPhysician.getCode());
        physician.setFirstName(entityPhysician.getFirstName());
        physician.setLastName(entityPhysician.getLastName());
        physician.setMiddleName(entityPhysician.getMiddleName());
        physician.setPrefix(entityPhysician.getPrefix());
        physician.setSuffix(entityPhysician.getSuffix());

        return physician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Physician physician = (Physician) o;

        return Objects.equals(code, physician.code)
                && Objects.equals(firstName, physician.firstName)
                && Objects.equals(lastName, physician.lastName)
                && Objects.equals(middleName, physician.middleName)
                && Objects.equals(suffix, physician.suffix)
                && Objects.equals(prefix, physician.prefix);
    }

    private boolean isEmpty() {
        return Stream.of(
                        firstName,
                        code,
                        lastName,
                        middleName
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String pathologist = addIndentation(indentationLevel) + "<Physician>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            pathologist += addIndentation(indentationLevel) + "<Code>" +  nullSafe(code) + "</Code>\n"
                    + addIndentation(indentationLevel) + "<FirstName>" +  nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<LastName>" +  nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" +  nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<Prefix>" +  nullSafe(prefix) + "</Prefix>\n"
                    + addIndentation(indentationLevel) + "<Suffix>" +  nullSafe(suffix) + "</Suffix>\n";

            indentationLevel --;
        }

        pathologist += addIndentation(indentationLevel) + "</Physician>";
        return pathologist;
    }
}
