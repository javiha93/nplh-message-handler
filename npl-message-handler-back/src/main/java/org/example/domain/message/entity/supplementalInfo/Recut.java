package org.example.domain.message.entity.supplementalInfo;

import lombok.Data;
import org.example.domain.message.entity.SupplementalInfo;

@Data
public class Recut extends SupplementalInfo {

    public Recut(String value, String artifact) {
        this.setType("RECUT");
        this.setValue(value);
        this.setArtifact(artifact);
    }
    public Recut(String value) {
        this.setType("RECUT");
        this.setValue(value);
    }

    public static Recut Default() {
        return new Recut("RecultValue", "SLIDE");
    }
}
