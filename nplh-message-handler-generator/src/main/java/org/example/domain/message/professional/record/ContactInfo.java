package org.example.domain.message.professional.record;

import lombok.Data;
import org.example.domain.message.Reflection;

@Data
public class ContactInfo extends Reflection implements Cloneable{
    protected String address;
    protected String address2;
    protected String city;
    protected String country;
    protected String state;
    protected String zip;
    protected String homePhone;
    protected String workPhone;
    protected String mobile;
    protected String email;

    @Override
    public ContactInfo clone() {
        try {
            return (ContactInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for ContactInfo", e);
        }
    }
}
