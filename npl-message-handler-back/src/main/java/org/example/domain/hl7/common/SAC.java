package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.entity.Order;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class SAC extends HL7Segment {

    @HL7Position(position = 7)
    private String registerDateTime;

    public static SAC Default() {
        SAC sac = new SAC();
        sac.registerDateTime = "20240518170503";

        return sac;
    }
    public static SAC Default(SAC sac) {
        sac.registerDateTime = "20240518170503";

        return sac;
    }

    public static SAC FromOrder(Order order, SAC sac) {
        sac.registerDateTime = order.getRegisterDate()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return sac;
    }

    @Override
    public String toString() {
        String value = "SAC|||||||" +
                nullSafe(registerDateTime);

        return cleanSegment(value);
    }
}
