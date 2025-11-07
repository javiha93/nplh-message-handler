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
public class OBX extends HL7Segment {

    @HL7Position(position = 1)
    private String segmentNumber;

    @HL7Position(position = 2)
    private String actionCode;

    @HL7Position(position = 3, subPosition = 1)
    private String slideID;

    @HL7Position(position = 5, subPosition = 1)
    private String controlSlideType;

    @HL7Position(position = 5, subPosition = 2)
    private String controlSlideTypeDescription;

    @HL7Position(position = 5, subPosition = 3)
    private String scoringType;

    @HL7Position(position = 5, subPosition = 4)
    private String cloneType;

    @HL7Position(position = 5, subPosition = 5)
    private String vendor;


    public static OBX Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber) {
        OBX obx = new OBX();

        obx.segmentNumber = segmentNumber;
        obx.actionCode = "CE";
        obx.slideID = sampleId + ";" + specimenId + ";" + blockId + ";" + slideId;
        obx.controlSlideType = "ControlSlideType";
        obx.controlSlideTypeDescription = "ControlSlideDesc";
        obx.scoringType = "ScroingType";
        obx.cloneType = "cloneType";
        obx.vendor = "vendor";

        return obx;
    }

    public static OBX FromMessage(Slide slide, Message message, int segmentNumber, OBX obx) {
        Block block = slide.getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        obx.segmentNumber = String.valueOf(segmentNumber);
        obx.actionCode = "CE";
        obx.slideID = slide.getId();
        obx.controlSlideType = slide.getControl().getName();
        obx.controlSlideTypeDescription = slide.getControl().getDescription();
        obx.scoringType = slide.getControl().getScoring();
        obx.cloneType = slide.getControl().getClone();
        obx.vendor = slide.getControl().getVendor();

        return obx;
    }

    public static OBX Default(String sampleId, String specimenId, String blockId, String slideId, String segmentNumber, OBX obx) {
        obx.segmentNumber = segmentNumber;
        obx.actionCode = "CE";
        obx.slideID = sampleId + ";" + specimenId + ";" + blockId + ";" + slideId;
        obx.controlSlideType = "ControlSlideType";
        obx.controlSlideTypeDescription = "ControlSlideDesc";
        obx.scoringType = "ScroingType";
        obx.cloneType = "cloneType";
        obx.vendor = "vendor";

        return obx;
    }

    @Override
    public String toString() {
        String value = "OBX|" +
                nullSafe(segmentNumber) + "|" +                                                                                                                                      // 1
                nullSafe(actionCode) + "|" +                                                                                                                                         // 2
                nullSafe(slideID) + "||" +                                                                                                                                           // 3
                nullSafe(controlSlideType) + "^" + nullSafe(controlSlideTypeDescription) + "^" + nullSafe(scoringType) +  "^" + nullSafe(cloneType) + "^" + nullSafe(vendor) + "|";  // 5                                                                                               // 7

        return cleanSegment(value);
    }
}
