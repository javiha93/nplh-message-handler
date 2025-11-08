
package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Technician extends WSSegment {
    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "LastName")
    private String lastName;
    @JacksonXmlProperty(localName = "MiddleName")
    private String middleName;
    @JacksonXmlProperty(localName = "UserId")
    private String userId;

    public static Technician FromTechnician(org.example.domain.message.professional.Technician entityTechnician) {
        if (entityTechnician.isEmpty()) {
            return null;
        }

        Technician technician = new Technician();

        technician.firstName = entityTechnician.getFirstName();
        technician.lastName = entityTechnician.getLastName();
        technician.middleName = entityTechnician.getMiddleName();
        technician.userId = entityTechnician.getCode();

        return technician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Technician technician = (Technician) o;

        return Objects.equals(firstName, technician.firstName)
                && Objects.equals(lastName, technician.lastName)
                && Objects.equals(middleName, technician.middleName)
                && Objects.equals(userId, technician.userId);
    }

    private boolean isEmpty() {
        return Stream.of(firstName, lastName, middleName, userId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String technician = addIndentation(indentationLevel) + "<Technician>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            technician += addIndentation(indentationLevel) + "<FirstName>" + nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<LastName>" + nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" + nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<UserId>" + nullSafe(userId) + "</UserId>\n";

            indentationLevel --;
        }

        technician += addIndentation(indentationLevel) + "</Technician>";
        return technician;
    }
}
