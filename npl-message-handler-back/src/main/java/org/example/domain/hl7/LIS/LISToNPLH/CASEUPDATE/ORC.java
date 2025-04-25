package org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE;

import org.example.domain.message.entity.Order;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID, String orderStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("SC");
        orc.setActionCode("CM");
        orc.setOrderStatus(orderStatus);

        return orc;
    }

    public static ORC FromMessage(Order order, String orderStatus) {
        ORC orc = (ORC) FromMessage(order, new ORC());

        orc.setMessageCode("SC");
        orc.setActionCode("CM");
        orc.setOrderStatus(orderStatus);

        return orc;
    }

    @Override
    public String toString() {
        return toStringCaseUpdate();
    }
}
