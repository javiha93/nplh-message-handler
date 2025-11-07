package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Physician extends WSSegment {
    private String code;
    private String firstName;
    private String lastName;
    private String middleName;
    private String suffix;

    public static Physician FromPhysician(org.example.domain.message.professional.Physician entityPhysician) {
        Physician physician = new Physician();

        physician.setCode(entityPhysician.getCode());
        physician.setFirstName(entityPhysician.getFirstName());
        physician.setLastName(entityPhysician.getLastName());
        physician.setMiddleName(entityPhysician.getMiddleName());
        physician.setSuffix(entityPhysician.getSuffix());

        return physician;
    }

    private boolean isEmpty() {
        return Stream.of(
                        firstName,
                        code,
                        lastName,
                        middleName,
                        suffix
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }
    public String toString(int indentationLevel) {
        return toString(indentationLevel, "RequestingPhysician");
    }


    public String toString(int indentationLevel, String tag) {
        String physician = addIndentation(indentationLevel) + "<" + tag + ">\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            physician += addIndentation(indentationLevel) + "<Code>" +  nullSafe(code) + "</Code>\n"
                    + addIndentation(indentationLevel) + "<FirstName>" +  nullSafe(firstName) + "</FirstName>\n"
                    + addIndentation(indentationLevel) + "<LastName>" +  nullSafe(lastName) + "</LastName>\n"
                    + addIndentation(indentationLevel) + "<MiddleName>" +  nullSafe(middleName) + "</MiddleName>\n"
                    + addIndentation(indentationLevel) + "<Suffix>" +  nullSafe(suffix) + "</Suffix>\n";

            indentationLevel --;
        }

        physician += addIndentation(indentationLevel) + "</" + tag + ">";
        return physician;
    }
}
