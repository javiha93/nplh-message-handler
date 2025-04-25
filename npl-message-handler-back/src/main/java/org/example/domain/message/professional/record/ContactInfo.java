package org.example.domain.message.professional.record;

import lombok.Data;
import org.example.domain.message.Reflection;

@Data
public class ContactInfo extends Reflection implements Cloneable{
    String address;
    String city;
    String country;
    String state;
    String zip;
    String homePhone;
    String workPhone;
    String mobile;
    String email;

    @Override
    public ContactInfo clone() {
        try {
            return (ContactInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for ContactInfo", e);
        }
    }
}
