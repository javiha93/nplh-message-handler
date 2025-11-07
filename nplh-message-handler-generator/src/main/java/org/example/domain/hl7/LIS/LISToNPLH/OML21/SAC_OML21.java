package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.common.SAC;
import org.example.domain.message.entity.Order;

@Data
@NoArgsConstructor
public class SAC_OML21 extends SAC {

    @HL7Position(position = 7)
    private String registerDateTime;

    public static SAC_OML21 Default() {
        return (SAC_OML21) Default(new SAC_OML21());
    }

    public static SAC_OML21 FromOrder(Order order) {
        return (SAC_OML21) SAC.FromOrder(order, new SAC_OML21());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
