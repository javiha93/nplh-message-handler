package org.example.domain.hl7.LIS.LISToNPLH.OML21;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.professional.Physician;

@Data
@NoArgsConstructor
public class PV1 extends org.example.domain.hl7.common.PV1 {
    public static PV1 Default() {
        return (PV1) org.example.domain.hl7.common.PV1.Default(new PV1());
    }

    public static PV1 FromPhysician(Physician physician) {return (PV1) org.example.domain.hl7.common.PV1
            .FromPhysician(physician, new PV1());}
    @Override
    public String toString() {
        return super.toString();
    }
}
