package org.example.domain.message.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Reflection;

@Data
@NoArgsConstructor
public class StainProtocol extends Reflection implements Cloneable {
    private String number;
    private String name;
    private String identifier;
    private String description;

    public static StainProtocol Default(String number, String name, String description) {
        StainProtocol stainProtocol = new StainProtocol();

        stainProtocol.setNumber(number);
        stainProtocol.setName(name);
        stainProtocol.setIdentifier("STAIN");
        stainProtocol.setDescription(description);

        return stainProtocol;
    }

    public static StainProtocol Default() {
        StainProtocol stainProtocol = new StainProtocol();

        stainProtocol.setNumber("1123");
        stainProtocol.setName("H. Pylori1");
        stainProtocol.setIdentifier("STAIN");

        return stainProtocol;
    }

    @Override
    public StainProtocol clone() {
        try {
            return (StainProtocol) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Technician", e);
        }
    }
}
