package org.example.domain.message.professional.record;

import lombok.Data;
import org.example.domain.message.MessageHeader;

@Data
public class PersonInfo extends ContactInfo implements Cloneable {
    protected String code;
    protected String lastName;
    protected String firstName;
    protected String middleName;

    @Override
    public PersonInfo clone() {
        return (PersonInfo) super.clone();
    }
}
