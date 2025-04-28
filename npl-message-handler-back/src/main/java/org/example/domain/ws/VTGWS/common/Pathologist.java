package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Pathologist extends WSSegment {
    private String code;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String workPhone;

    public static Pathologist FromPathologist(org.example.domain.message.professional.Pathologist entityPathologist) {
        Pathologist pathologist = new Pathologist();

        pathologist.setCode(entityPathologist.getCode());
        pathologist.setEmail(entityPathologist.getEmail());
        pathologist.setFirstName(entityPathologist.getFirstName());
        pathologist.setLastName(entityPathologist.getLastName());
        pathologist.setMiddleName(entityPathologist.getMiddleName());
        pathologist.setWorkPhone(entityPathologist.getWorkPhone());

        return pathologist;
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
