package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

@Data
@NoArgsConstructor
public class ORC extends HL7Segment {

    @HL7Position(position = 1)
    private String actionCode;

    @HL7Position(position = 1)
    private String messageCode;

    @HL7Position(position = 2, subPosition = 1)
    private String sampleID;

    @HL7Position(position = 2, subPosition = 3)
    private String extSampleID;

    @HL7Position(position = 4)
    private String slideId;

    @HL7Position(position = 4)
    private String blockId;

    @HL7Position(position = 4)
    private String specimenId;

    @HL7Position(position = 17, subPosition = 1)
    private String origin;

    @HL7Position(position = 17, subPosition = 2)
    private String originDesc;

    @HL7Position(position = 21, subPosition = 1)
    private String facilityCode;

    @HL7Position(position = 21, subPosition = 2)
    private String facilityName;

    @HL7Position(position = 12)
    private String orderStatus;

    @HL7Position(position = 12)
    private String specimenStatus;

    @HL7Position(position = 12)
    private String blockStatus;

    @HL7Position(position = 12)
    private String slideStatus;

    public static ORC Default(String sampleID) {
        ORC orc = new ORC();

        orc.actionCode = "NW";
        orc.sampleID = sampleID;
        orc.facilityCode = "VMSI";
        orc.facilityName = "Ventana Medical Systems";

        return orc;
    }

    public static ORC Default(String sampleID, ORC orc) {
        orc.actionCode = "NW";
        orc.sampleID = sampleID;
        orc.facilityCode = "VMSI";
        orc.facilityName = "Ventana Medical Systems";

        return orc;
    }

    public static ORC FromMessage(Slide slide, Message message, ORC orc) {
        Block block = slide.getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        orc.actionCode = slide.getActionCode();
        orc.sampleID = order.getSampleId();
        orc.slideId = slide.getId();
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    public static ORC FromMessage(Block block, Message message, ORC orc) {
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        orc.sampleID = order.getSampleId();
        orc.blockId = block.getId();
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    public static ORC FromMessage(Specimen specimen, String sampleID, ORC orc) {
        orc.sampleID = sampleID;
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    public static ORC FromMessage(Order order, ORC orc) {
        Specimen specimen = order.getFirstSpecimen();

        orc.sampleID = order.getSampleId();
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    @Override
    public String toString() {
        String value = "ORC|" +
                nullSafe(actionCode) + "|" +                                                    // 1
                nullSafe(sampleID) + "^^" + nullSafe(extSampleID) + "||||||||||||||" +          // 2
                nullSafe(origin) + "|" + nullSafe(originDesc) + "||||" +                        // 17
                nullSafe(facilityCode) + "^" + nullSafe(facilityName) + "|";                    // 21

        return cleanSegment(value);
    }

    protected String toStringCaseUpdate() {
        String value = "ORC|" +
                nullSafe(messageCode) + "|" +        // 1
                nullSafe(sampleID) + "||" +          // 2
                nullSafe(sampleID) + "^CASE|" +      // 4
                nullSafe(actionCode) + "||||||||||||||||||||" +           // 5
                nullSafe(orderStatus) + "|";         // 21

        return cleanSegment(value);
    }

    protected String toStringSlideUpdate() {
        String value = "ORC|" +
                nullSafe(messageCode) + "|" +        // 1
                nullSafe(sampleID) + "||" +          // 2
                nullSafe(slideId) + "^SLIDE|" +      // 4
                nullSafe(actionCode) + "||||||||||||||||||||" +           // 5
                nullSafe(slideStatus) + "|";         // 21

        return cleanSegment(value);
    }

}
