package org.example.domain.hl7.VTG.VTGToNPLH.OEWF;

import lombok.Data;

@Data
public class OSegment {
    ORC orc;
    OBR obr;
    OBX obx;

    public OSegment(ORC orc, OBR obr, OBX obx) {
        this.orc = orc;
        this.obr = obr;
        this.obx = obx;
    }

    public OSegment(ORC orc, OBR obr) {
        this.orc = orc;
        this.obr = obr;
    }
}
