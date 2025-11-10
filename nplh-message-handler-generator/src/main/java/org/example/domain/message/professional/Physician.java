package org.example.domain.message.professional;

import lombok.Data;
import org.example.domain.message.professional.record.ContactInfo;
import org.example.domain.message.professional.record.PersonInfo;

import java.util.stream.Stream;

@Data
public class Physician extends PersonInfo implements Cloneable {
    private String suffix;
    private String prefix;


    public static Physician Default() {

        Physician physician = new Physician();
        physician.setCode("PhyiID");
        physician.setLastName("PhyLastName");
        physician.setFirstName("PhyFirstName");
        physician.setMiddleName("PhymiddleName");
        physician.setAddress("Phyaddress");
        physician.setCity("Phycity");
        physician.setCountry("Phycountry");
        physician.setState("Phystate");
        physician.setZip("Phyzip");
        physician.setHomePhone("Phyhometel");
        physician.setWorkPhone("Phyworktel");
        physician.setMobile("Phymobiletel");
        physician.setSuffix("Physuf");
        physician.setPrefix("Phypref");

        return physician;
    }
    @Override
    public Physician clone() {
        return (Physician) super.clone();
    }

    public boolean isEmpty() {
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
}
