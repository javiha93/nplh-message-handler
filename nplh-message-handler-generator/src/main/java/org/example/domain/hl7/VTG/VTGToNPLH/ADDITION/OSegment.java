package org.example.domain.hl7.VTG.VTGToNPLH.ADDITION;

import lombok.Data;

@Data
public class OSegment {
    ORC orc;
    OBR obr;

    public OSegment(ORC orc, OBR obr) {
        this.orc = orc;
        this.obr = obr;
    }
}
