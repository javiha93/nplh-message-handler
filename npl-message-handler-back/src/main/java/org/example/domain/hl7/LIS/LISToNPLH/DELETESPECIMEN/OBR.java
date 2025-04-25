package org.example.domain.hl7.LIS.LISToNPLH.DELETESPECIMEN;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

public class OBR extends org.example.domain.hl7.common.OBR {

    public static OBR Default(String specimenId) {
        OBR obr = new OBR();

        obr.setSpecimenID(specimenId);

        return obr;
    }

    public static OBR FromMessage(Specimen specimen, Message message) {
        return (OBR) FromMessage(specimen, message, new OBR());
    }

    @Override
    public String toString() {
        return toStringDeleteSpecimen();
    }
}
