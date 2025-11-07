package org.example.domain.hl7.VTG.NPLHToVTG;

import lombok.Data;

@Data
public class OSegment {
    ORC_OML21 orc;
    OBR_OML21 obr;

    public OSegment(ORC_OML21 orc, OBR_OML21 obr) {
        this.orc = orc;
        this.obr = obr;
    }
}
