package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import org.example.domain.hl7.common.SAC;
import org.example.domain.message.entity.Order;

import java.util.Objects;

@Data
public class SAC_SLIDE_UPDATE extends SAC {

    String caseId;
    public static SAC_SLIDE_UPDATE fromOrder(Order order) {
        SAC_SLIDE_UPDATE sac = (SAC_SLIDE_UPDATE) SAC.FromOrder(order, new SAC_SLIDE_UPDATE());

        sac.setCaseId(order.getSampleId());

        return sac;
    }

    @Override
    public String toString() {
        String value = "SAC||" + nullSafe(caseId) + "|||||" +
                nullSafe(registerDateTime);

        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SAC_SLIDE_UPDATE sac = (SAC_SLIDE_UPDATE) o;

        return Objects.equals(caseId, sac.caseId);
    }

    protected static SAC_SLIDE_UPDATE parseSAC(String line) {
        SAC_SLIDE_UPDATE sac = (SAC_SLIDE_UPDATE) SAC.parseSAC(line, new SAC_SLIDE_UPDATE());

        String[] fields = line.split("\\|");
        if (fields.length > 2) {
            sac.setCaseId(getFieldValue(fields, 2));
        }

        return sac;
    }
}
