
package org.example.domain.message.entity.list;

import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.SupplementalInfo;

import java.util.ArrayList;
import java.util.List;

@Data
public class SupplementalInfoList extends Reflection implements Cloneable {
    private List<SupplementalInfo> supplementalInfoList;

    public SupplementalInfoList() {
        // Initialize with an empty ArrayList but no default items
        this.supplementalInfoList = new ArrayList<>();
    }
    
    @Override
    public SupplementalInfoList clone() {
        try {
            SupplementalInfoList cloned = (SupplementalInfoList) super.clone();
            if (supplementalInfoList == null) {
                return cloned;
            }
            List<SupplementalInfo> clonedSupplementalInfoList = new ArrayList<>();
            for (SupplementalInfo supplementalInfo : this.supplementalInfoList) {
                clonedSupplementalInfoList.add(supplementalInfo.clone());
            }
            cloned.setSupplementalInfoList(clonedSupplementalInfoList);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for SupplementalInfoList", e);
        }
    }

}
