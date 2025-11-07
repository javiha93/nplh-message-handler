package org.example.domain.message.professional.record;

import lombok.Data;
import org.example.domain.message.MessageHeader;

@Data
public class PersonInfo extends ContactInfo implements Cloneable {
    String code;
    String lastName;
    String firstName;
    String middleName;

    @Override
    public PersonInfo clone() {
        return (PersonInfo) super.clone();
    }
}
