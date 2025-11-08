package org.example.domain.hl7.VTG.NPLHToVTG;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.PV1;
import org.example.domain.message.professional.Physician;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PV1_OML21 extends PV1 {
    public static PV1_OML21 Default() {
        return (PV1_OML21) PV1.Default(new PV1_OML21());
    }

    public static PV1_OML21 FromPhysician(Physician physician) {
        return (PV1_OML21) PV1.FromPhysician(physician, new PV1_OML21());
    }
    @Override
    public String toString() {
        String value = "PV1|||||||" +
                nullSafe(requestorCode) + "^" + nullSafe(requestorLastName) + "^" + nullSafe(requestorFirstName) + "^" + nullSafe(requestorMiddleName) + "^" + nullSafe(requestorSuffix) + "^" + nullSafe(requestorPrefix) + "|";

        return cleanSegment(value);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PV1_OML21 pv1 = (PV1_OML21) o;
        return Objects.equals(requestorCode, pv1.requestorCode) &&
                Objects.equals(requestorLastName, pv1.requestorLastName) &&
                Objects.equals(requestorFirstName, pv1.requestorFirstName) &&
                Objects.equals(requestorMiddleName, pv1.requestorMiddleName) &&
                Objects.equals(requestorSuffix, pv1.requestorSuffix) &&
                Objects.equals(requestorPrefix, pv1.requestorPrefix);
    }

    protected static PV1_OML21 parsePV1(String line) {
        return (PV1_OML21) PV1.parsePV1(line, new PV1_OML21());
    }
}
