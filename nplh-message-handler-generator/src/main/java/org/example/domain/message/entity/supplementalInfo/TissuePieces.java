package org.example.domain.message.entity.supplementalInfo;

import lombok.Data;
import org.example.domain.message.entity.SupplementalInfo;

@Data
public class TissuePieces extends SupplementalInfo {

    public TissuePieces(String value, String artifact) {
        this.setType("TISSUEPIECES");
        this.setValue(value);
        this.setArtifact(artifact);
    }

    public TissuePieces(String value) {
        this.setType("TISSUEPIECES");
        this.setValue(value);
    }

    public static TissuePieces Default() {
        return new TissuePieces("6", "BLOCK");
    }
}
