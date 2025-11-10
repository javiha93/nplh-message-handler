package org.example.domain.hl7.LIS.LISToNPLH.DELETESPECIMEN;

import org.example.domain.hl7.HL7Position;
import org.example.domain.message.entity.Specimen;

public class ORC extends org.example.domain.hl7.common.ORC {

    @HL7Position(position = 1)
    private String messageCode;

    @HL7Position(position = 12)
    private String orderStatus;

    public static ORC Default(String sampleID, String specimenId) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("CA");
        orc.setActionCode("CA");
        orc.setSpecimenId(specimenId);
        orc.setOrderStatus("DELETE");

        return orc;
    }

    public static ORC FromMessage(Specimen specimen, String sampleId) {
        ORC orc = (ORC) fromMessage(specimen, sampleId, new ORC());
        orc.setActionCode("CA");

        orc.messageCode = "CA";
        orc.setSpecimenId(specimen.getId());
        orc.orderStatus = "DELETE";

        return orc;
    }

    @Override
    public String toString() {
        String value = "ORC|" +
                nullSafe(messageCode) + "|" +         // 1
                nullSafe(getSampleID()) + "||" +           // 2
                nullSafe(getSpecimenId()) + "^SPECIMEN|" + // 4
                "CA||||||||||||||||||||" +            // 5
                nullSafe(orderStatus) + "|";          // 21

        return cleanSegment(value);
    }
}
