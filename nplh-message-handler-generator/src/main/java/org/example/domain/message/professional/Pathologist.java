package org.example.domain.message.professional;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.professional.record.ContactInfo;
import org.example.domain.message.professional.record.PersonInfo;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Pathologist extends PersonInfo implements  Cloneable {
    private String suffix;
    private String prefix;

    public static Pathologist Default() {

        Pathologist pathologist = new Pathologist();
        pathologist.setCode("PatoId");
        pathologist.setLastName("PatoLastName");
        pathologist.setFirstName("PatoFirstName");
        pathologist.setMiddleName("PatoMiddleName");
        pathologist.setAddress("PatoAddress");
        pathologist.setCity("Patocity");
        pathologist.setCountry("Patocountry");
        pathologist.setState("Patostate");
        pathologist.setZip("Patozip");
        pathologist.setHomePhone("Patohometel");
        pathologist.setWorkPhone("Patoworktel");
        pathologist.setMobile("Patomobiletel");
        pathologist.setEmail("Patomail@e.com");
        pathologist.setSuffix("Patosuf");
        pathologist.setPrefix("Patopref");

        return pathologist;
    }

    public boolean isEmpty() {
        return Stream.of(
                        suffix,
                        prefix,
                        code,
                        lastName,
                        firstName,
                        middleName
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public Pathologist clone() {
        return (Pathologist) super.clone();
    }
}
