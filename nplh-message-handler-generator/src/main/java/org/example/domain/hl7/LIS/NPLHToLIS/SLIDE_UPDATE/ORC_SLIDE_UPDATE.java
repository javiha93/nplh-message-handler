package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.OBR;
import org.example.domain.hl7.common.ORC;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ORC_SLIDE_UPDATE extends ORC {

    private String vipOrderStatus;

    public static ORC_SLIDE_UPDATE fromSlide(Slide slide, Message message, String slideStatus) {
        ORC_SLIDE_UPDATE orc = (ORC_SLIDE_UPDATE) ORC.fromMessage(slide, message, new ORC_SLIDE_UPDATE());
        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setSlideStatus(slideStatus);

        return orc;
    }

    public static ORC_SLIDE_UPDATE fromSpecimen(Specimen specimen, Message message) {
        ORC_SLIDE_UPDATE orc = new ORC_SLIDE_UPDATE();

        orc.setSampleID(message.getOrder().getSampleId());
        orc.setExtSampleID(message.getOrder().getExtSampleId());

        orc.setVipOrderStatus("CM");
        //TODO check logic
        orc.setOrderStatus("B");
        orc.setFacilityCode(specimen.getFacilityCode());
        orc.setFacilityName(specimen.getFacilityName());

        return orc;
    }

    @Override
    public String toString() {
        String value;
        if (messageCode != null && messageCode.equals("SC")) {
            value = "ORC|" +
                    nullSafe(getMessageCode()) + "||" +        // 1
                    nullSafe(getSlideId()) + "||" +      // 3
                    nullSafe(getActionCode()) + "||||||||||||||||||||" + // 5
                    nullSafe(getSlideStatus()) + "|";         // 21
        } else {
            value = "ORC||" +
                    nullSafe(getSampleID()) + "|||" +
                    nullSafe(getVipOrderStatus()) + "|" +
                    nullSafe(getOrderStatus()) + "|||||||||||||||" +
                    nullSafe(getFacilityCode()) + "^" + nullSafe(getFacilityName());
        }

        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ORC_SLIDE_UPDATE orc = (ORC_SLIDE_UPDATE) o;

        if (messageCode != null && messageCode.equals("SC")) {
            return Objects.equals(actionCode, orc.actionCode) &&
                    Objects.equals(messageCode, orc.messageCode) &&
                    Objects.equals(slideId, orc.slideId) &&
                    Objects.equals(slideStatus, orc.slideStatus);
        } else {
            return Objects.equals(sampleID, orc.sampleID) &&
                    Objects.equals(vipOrderStatus, orc.vipOrderStatus) &&
                    Objects.equals(orderStatus, orc.orderStatus) &&
                    Objects.equals(facilityCode, orc.facilityCode) &&
                    Objects.equals(facilityName, orc.facilityName);
        }        
    }

    protected static ORC_SLIDE_UPDATE parseORCVSSUpdate(String line) {
        ORC_SLIDE_UPDATE orc = new ORC_SLIDE_UPDATE();

        String[] fields = line.split("\\|");

        if (fields.length > 2) {
            orc.setSampleID(getFieldValue(fields, 2));
        }

        if (fields.length > 5) {
            orc.setVipOrderStatus(getFieldValue(fields, 5));
        }

        if (fields.length > 6) {
            orc.setOrderStatus(getFieldValue(fields, 6));
        }

        if (fields.length > 21 && fields[21] != null && !fields[21].isEmpty()) {
            String[] facility = fields[21].split("\\^");
            if (facility.length > 0) orc.setFacilityCode(facility[0]);
            if (facility.length > 1) orc.setFacilityName(facility[1]);
        }

        return orc;
    }

    protected static ORC_SLIDE_UPDATE parseORC(String line) {
        ORC_SLIDE_UPDATE orc = new ORC_SLIDE_UPDATE();

        String[] fields = line.split("\\|");

        if (fields.length > 1) {
            orc.setMessageCode(getFieldValue(fields, 1));
        }

        if (fields.length > 3) {
            orc.setSlideId(getFieldValue(fields, 3));
        }

        if (fields.length > 5) {
            orc.setActionCode(getFieldValue(fields, 5));
        }

        if (fields.length > 25) {
            orc.setSlideStatus(getFieldValue(fields, 25));
        }

        if (!orc.getMessageCode().equals("SC") || !orc.getActionCode().equals("IP")) {
            throw new RuntimeException("Unable to parse SLIDE UPDATE from VTG to LIS");
        }

        return orc;
    }
}
