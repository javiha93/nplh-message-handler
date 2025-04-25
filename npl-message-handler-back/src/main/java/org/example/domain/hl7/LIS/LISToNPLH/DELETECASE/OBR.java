
package org.example.domain.hl7.LIS.LISToNPLH.DELETECASE;

import org.example.domain.hl7.HL7Position;
import org.example.domain.message.entity.Order;

public class OBR extends org.example.domain.hl7.common.OBR {

    @HL7Position(position = 2)
    private String sampleId;

    public static OBR Default(String sampleID) {
        OBR obr = new OBR();

        obr.sampleId = sampleID;

        return obr;
    }

    public static OBR FromMessage(Order order) {
        OBR obr = (OBR) FromMessage(order, new OBR());

        obr.sampleId = order.getSampleId();

        return obr;
    }

    @Override
    public String toString() {
        String value = "OBR||" +
                nullSafe(sampleId) + "|";          // 2

        return cleanSegment(value);
    }
}
