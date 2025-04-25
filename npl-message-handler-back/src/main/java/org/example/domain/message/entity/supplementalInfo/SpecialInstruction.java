package org.example.domain.message.entity.supplementalInfo;

import lombok.Data;
import org.example.domain.message.entity.SupplementalInfo;

@Data
public class SpecialInstruction extends SupplementalInfo {

    public SpecialInstruction(String value, String artifact) {
        this.setType("SPECIALINSTRUCTION");
        this.setValue(value);
        this.setArtifact(artifact);
    }

    public SpecialInstruction(String value) {
        this.setType("SPECIALINSTRUCTION");
        this.setValue(value);
    }

    public static SpecialInstruction Default() {
       return new SpecialInstruction("SPECIALINSTRUCTIONVALUE", "PART");
    }
}
