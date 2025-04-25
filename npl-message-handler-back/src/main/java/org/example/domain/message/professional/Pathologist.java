package org.example.domain.message.professional;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.professional.record.ContactInfo;
import org.example.domain.message.professional.record.PersonInfo;

@Data
@NoArgsConstructor
public class Pathologist extends PersonInfo implements  Cloneable {
    private String suffix;
    private String prefix;

    public static Pathologist Default() {

        Pathologist pathologist = new Pathologist();
        pathologist.setCode("IndiID");
        pathologist.setLastName("ILastName");
        pathologist.setFirstName("IFirstName");
        pathologist.setMiddleName("ImiddleName");
        pathologist.setAddress("Iaddress");
        pathologist.setCity("city");
        pathologist.setCountry("Icountry");
        pathologist.setState("state");
        pathologist.setZip("zipcode");
        pathologist.setHomePhone("hometel");
        pathologist.setWorkPhone("worktel");
        pathologist.setMobile("mobiletel");
        pathologist.setEmail("Imail@e.com");
        pathologist.setSuffix("Isufix");
        pathologist.setPrefix("Iprefix");

        return pathologist;
    }
    @Override
    public Pathologist clone() {
        return (Pathologist) super.clone();
    }
}
