package org.example.domain.message.professional;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.professional.record.PersonInfo;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Technician extends PersonInfo implements Cloneable {

    public static Technician Default() {

        Technician technician = new Technician();
        technician.setCode("IndiID");
        technician.setLastName("ILastName");
        technician.setFirstName("IFirstName");
        technician.setMiddleName("ImiddleName");

        return technician;
    }

    public boolean isEmpty() {
        return Stream.of(
                        code,
                        lastName,
                        firstName,
                        middleName
                )
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public Technician clone() {
        return (Technician) super.clone();
    }
}
