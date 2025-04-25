
package org.example.domain.message.entity.supplementalInfo;

import lombok.Data;
import org.example.domain.message.entity.SupplementalInfo;

@Data
public class QualityIssue extends SupplementalInfo {
    private String optionalType;
    private String optionalValue;

    public QualityIssue(String value) {
        this.setType("QUALITYISSUE");
        this.setValue(value);
    }

    public QualityIssue(String value, String artifact, String optionalValue) {
        this.setType("QUALITYISSUE");
        this.setValue(value);
        this.setArtifact(artifact);
        this.setOptionalType("RESOLUTION");
        this.setOptionalValue(optionalValue);
        // Also set the qualityIssueType and qualityIssueValue for consistency
        this.setQualityIssueType("RESOLUTION");
        this.setQualityIssueValue(optionalValue);
    }

    public QualityIssue(String value, String optionalValue) {
        this.setType("QUALITYISSUE");
        this.setValue(value);
        this.setOptionalType("RESOLUTION");
        this.setOptionalValue(optionalValue);
        // Also set the qualityIssueType and qualityIssueValue for consistency
        this.setQualityIssueType("RESOLUTION");
        this.setQualityIssueValue(optionalValue);
    }

    public static QualityIssue Default() {
        return new QualityIssue("Tissue found", "PART", "Continue processing");
    }
}
