package org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE;

import org.example.domain.message.entity.Specimen;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID, String specimenStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setSpecimenStatus(specimenStatus);

        return orc;
    }

    public static ORC FromMessage(Specimen specimen, String sampleId, String specimenStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.fromMessage(specimen, sampleId, new ORC());
        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setSpecimenStatus(specimenStatus);

        return orc;
    }

    @Override
    public String toString() {
            String value = "ORC|" +
                    nullSafe(getMessageCode()) + "||" +        // 1
                    nullSafe(getSpecimenId()) + "||" +      // 3
                    nullSafe(getActionCode()) + "||||||||||||||||||||" + // 5
                    nullSafe(getSpecimenStatus()) + "|";         // 21

            return cleanSegment(value);
    }
}
