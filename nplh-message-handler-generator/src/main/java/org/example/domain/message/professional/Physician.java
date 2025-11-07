package org.example.domain.message.professional;

import lombok.Data;
import org.example.domain.message.professional.record.ContactInfo;
import org.example.domain.message.professional.record.PersonInfo;

@Data
public class Physician extends PersonInfo implements Cloneable {
    private String suffix;
    private String prefix;


    public static Physician Default() {

        Physician physician = new Physician();
        physician.setCode("IndiID");
        physician.setLastName("ILastName");
        physician.setFirstName("IFirstName");
        physician.setMiddleName("ImiddleName");
        physician.setAddress("Iaddress");
        physician.setCity("city");
        physician.setCountry("Icountry");
        physician.setState("state");
        physician.setZip("zipcode");
        physician.setHomePhone("hometel");
        physician.setWorkPhone("worktel");
        physician.setMobile("mobiletel");
        physician.setEmail("Imail@e.com");
        physician.setSuffix("Isufix");
        physician.setPrefix("Iprefix");

        return physician;
    }
    @Override
    public Physician clone() {
        return (Physician) super.clone();
    }

}
