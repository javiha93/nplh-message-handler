package org.example.domain.message.entity.supplementalInfo;

import org.example.domain.message.entity.SupplementalInfo;

public class GrossDescription extends SupplementalInfo {
    public GrossDescription(String value, String artifact) {
        this.setType("GROSSDESCRIPTION");
        this.setValue(value);
        this.setArtifact(artifact);
    }

    public GrossDescription(String value) {
        this.setType("GROSSDESCRIPTION");
        this.setValue(value);
    }

    public static TissuePieces Default() {
        return new TissuePieces("GROSSDESCRIPTIONVALUE", "PART");
    }
}
