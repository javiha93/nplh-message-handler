package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;

@Data
public class OSegment {
    ORC_OML21 orc;
    OBR_OML21 obr;
    OBX_OML21 obx;

    public OSegment(ORC_OML21 orc, OBR_OML21 obr, OBX_OML21 obx) {
        this.orc = orc;
        this.obr = obr;
        this.obx = obx;
    }

    public OSegment(ORC_OML21 orc, OBR_OML21 obr) {
        this.orc = orc;
        this.obr = obr;
    }
}
