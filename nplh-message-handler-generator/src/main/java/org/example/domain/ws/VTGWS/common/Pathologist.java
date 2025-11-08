package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Pathologist extends WSSegment {
    @JacksonXmlProperty(localName = "Code")
    private String code;
    @JacksonXmlProperty(localName = "Email")
    private String email;
    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;
    @JacksonXmlProperty(localName = "LastName")
    private String lastName;
    @JacksonXmlProperty(localName = "MiddleName")
    private String middleName;
    @JacksonXmlProperty(localName = "WorkPhone")
    private String workPhone;

    public static Pathologist FromPathologist(org.example.domain.message.professional.Pathologist entityPathologist) {
        if (entityPathologist.isEmpty()) {
            return null;
        }

        Pathologist pathologist = new Pathologist();

        pathologist.setCode(entityPathologist.getCode());
        pathologist.setEmail(entityPathologist.getEmail());
        pathologist.setFirstName(entityPathologist.getFirstName());
        pathologist.setLastName(entityPathologist.getLastName());
        pathologist.setMiddleName(entityPathologist.getMiddleName());
        pathologist.setWorkPhone(entityPathologist.getWorkPhone());

        return pathologist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pathologist pathologist = (Pathologist) o;

        return Objects.equals(code, pathologist.code)
                && Objects.equals(email, pathologist.email)
                && Objects.equals(firstName, pathologist.firstName)
                && Objects.equals(lastName, pathologist.lastName)
                && Objects.equals(middleName, pathologist.middleName)
                && Objects.equals(workPhone, pathologist.workPhone);
    }

    private boolean isEmpty() {
        return Stream.of(
                        firstName,
                        code,
                        lastName,
                        middleName,
                        email,
                        workPhone
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        return toString(indentationLevel, "Pathologist");
    }

    public String toString(int indentationLevel, String tag) {
        String pathologist = addIndentation(indentationLevel) + "<" + tag + ">\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            pathologist += addIndentation(indentationLevel) + "<Code>" +  nullSafe(code) + "</Code>\n"
                    + addIndentation(indentationLevel) + "<Email>" +  nullSafe(email) + "</Email>\n"
                    + addIndentation(indentationLevel) + "<FirstName>" +  nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<LastName>" +  nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" +  nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<WorkPhone>" +  nullSafe(workPhone) + "</WorkPhone>\n";

            indentationLevel --;
        }

        pathologist += addIndentation(indentationLevel) + "</" + tag + ">";
        return pathologist;
    }
}
