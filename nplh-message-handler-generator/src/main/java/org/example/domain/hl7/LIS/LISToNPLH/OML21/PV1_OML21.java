package org.example.domain.hl7.LIS.LISToNPLH.OML21;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.PV1;
import org.example.domain.message.professional.Physician;

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
        return super.toString();
    }
}
