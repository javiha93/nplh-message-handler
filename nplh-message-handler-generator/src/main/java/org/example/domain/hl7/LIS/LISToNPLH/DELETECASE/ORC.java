package org.example.domain.hl7.LIS.LISToNPLH.DELETECASE;

import org.example.domain.message.entity.Order;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID) {

        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("CA");
        orc.setActionCode("CA");
        orc.setOrderStatus("DELETE");

        return orc;
    }

    public static ORC FromMessage(Order order) {
       ORC orc = (ORC) fromMessage(order, new ORC());

        orc.setMessageCode("CA");
        orc.setActionCode("CA");
        orc.setOrderStatus("DELETE");

        return orc;
    }

    @Override
    public String toString() {
        return toStringCaseUpdate();
    }
}
