package org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.message.entity.Order;

@Data
@NoArgsConstructor
public class SAC extends org.example.domain.hl7.common.SAC {

    @HL7Position(position = 7)
    private String registerDateTime;

    public static SAC Default() {
        return (SAC) org.example.domain.hl7.common.SAC.Default(new SAC());
    }

    public static SAC FromOrder(Order order) {
        return (SAC) org.example.domain.hl7.common.SAC.FromOrder(order, new SAC());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
