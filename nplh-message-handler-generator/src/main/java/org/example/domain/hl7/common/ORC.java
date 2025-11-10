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

import java.util.Objects;

@Data
@NoArgsConstructor
public class ORC extends HL7Segment {

    @HL7Position(position = 1)
    protected String actionCode;

    @HL7Position(position = 1)
    protected String messageCode;

    @HL7Position(position = 2, subPosition = 1)
    protected String sampleID;

    @HL7Position(position = 2, subPosition = 3)
    protected String extSampleID;

    @HL7Position(position = 4)
    protected String slideId;

    @HL7Position(position = 4)
    protected String blockId;

    @HL7Position(position = 4)
    protected String specimenId;

    @HL7Position(position = 17, subPosition = 1)
    protected String origin;

    @HL7Position(position = 17, subPosition = 2)
    protected String originDesc;

    @HL7Position(position = 21, subPosition = 1)
    protected String facilityCode;

    @HL7Position(position = 21, subPosition = 2)
    protected String facilityName;

    @HL7Position(position = 12)
    protected String orderStatus;

    @HL7Position(position = 12)
    protected String specimenStatus;

    @HL7Position(position = 12)
    protected String blockStatus;

    @HL7Position(position = 12)
    protected String slideStatus;

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

    public static ORC fromMessage(Slide slide, Message message, ORC orc) {
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

    public static ORC fromMessage(Block block, Message message, ORC orc) {
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        orc.sampleID = order.getSampleId();
        orc.blockId = block.getId();
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    public static ORC fromMessage(Specimen specimen, String sampleID, ORC orc) {
        orc.sampleID = sampleID;
        orc.facilityCode = specimen.getFacilityCode();
        orc.facilityName = specimen.getFacilityName();

        return orc;
    }

    public static ORC fromMessage(Order order, ORC orc) {
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
                nullSafe(sampleID) + "^^" + nullSafe(extSampleID) + "|||||||||||||||" +         // 2
                nullSafe(origin) + "^" + nullSafe(originDesc) + "||||" +                        // 17
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ORC orc = (ORC) o;
        return Objects.equals(actionCode, orc.actionCode) &&
                Objects.equals(messageCode, orc.messageCode) &&
                Objects.equals(sampleID, orc.sampleID) &&
                Objects.equals(extSampleID, orc.extSampleID) &&
                Objects.equals(origin, orc.origin) &&
                Objects.equals(originDesc, orc.originDesc) &&
                Objects.equals(facilityCode, orc.facilityCode) &&
                Objects.equals(facilityName, orc.facilityName);
    }

    protected static ORC parseORC(String line, ORC orc) {
        String[] fields = line.split("\\|");

        if (fields.length > 1) {
            orc.setActionCode(getFieldValue(fields, 1));
        }

        if (fields.length > 2) {
            orc.setSampleID(getFieldValue(fields, 2));
        }

        if (fields.length > 17 && fields[17] != null && !fields[17].isEmpty()) {
            String[] origin = fields[17].split("\\^");
            if (origin.length > 0) orc.setOrigin(origin[0]);
            if (origin.length > 1) orc.setOriginDesc(origin[1]);
        }

        if (fields.length > 21 && fields[21] != null && !fields[21].isEmpty()) {
            String[] facility = fields[21].split("\\^");
            if (facility.length > 0) orc.setFacilityCode(facility[0]);
            if (facility.length > 1) orc.setFacilityName(facility[1]);
        }

        return orc;
    }

}
