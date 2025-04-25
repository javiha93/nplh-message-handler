
package org.example.domain.message.entity;

import lombok.Data;
import org.example.domain.message.Reflection;

import java.util.List;

@Data
public class SupplementalInfo extends Reflection implements Cloneable {
    private String type;
    private String value;
    private String artifact;
    private String qualityIssueType;
    private String qualityIssueValue;
    private String optionalType;
    private String optionalValue;

    @Override
    public SupplementalInfo clone() {
        try {
            return (SupplementalInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Technician", e);
        }
    }

}
