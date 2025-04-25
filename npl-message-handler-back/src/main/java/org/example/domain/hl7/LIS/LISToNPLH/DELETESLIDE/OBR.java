package org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;

public class OBR extends org.example.domain.hl7.common.OBR {

    public static OBR Default(String slideId) {
        OBR obr = new OBR();

        obr.setSlideID(slideId);

        return obr;
    }

    public static OBR FromMessage(Slide slide, Message message) {
        return (OBR) FromMessage(slide, message, new OBR());
    }

    @Override
    public String toString() {
        return toStringDeleteSlide();
    }
}
