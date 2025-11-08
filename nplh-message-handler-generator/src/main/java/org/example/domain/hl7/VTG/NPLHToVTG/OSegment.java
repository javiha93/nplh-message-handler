package org.example.domain.hl7.VTG.NPLHToVTG;

import lombok.Data;

import java.util.Objects;

@Data
public class OSegment {
    ORC_OML21 orc;
    OBR_OML21 obr;

    public OSegment(ORC_OML21 orc, OBR_OML21 obr) {
        this.orc = orc;
        this.obr = obr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OSegment segment = (OSegment) o;
        return Objects.equals(orc, segment.orc) &&
                Objects.equals(obr, segment.obr);

    }
}
