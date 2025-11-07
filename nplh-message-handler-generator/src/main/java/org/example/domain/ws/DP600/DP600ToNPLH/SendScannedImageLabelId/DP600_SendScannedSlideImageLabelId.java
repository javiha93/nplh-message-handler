package org.example.domain.ws.DP600.DP600ToNPLH.SendScannedImageLabelId;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class DP600_SendScannedSlideImageLabelId extends WSSegment {

    private String slideId;

    public static DP600_SendScannedSlideImageLabelId FromMessage(Slide slide) {
        DP600_SendScannedSlideImageLabelId sendScannedSlide = new DP600_SendScannedSlideImageLabelId();

        sendScannedSlide.setSlideId(slide.getId());

        return sendScannedSlide;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<sendScannedSlideImageLabelId>\n" +
                addIndentation(indentationLevel) + "<arg0>" + slideId + "</arg0>\n" +
                "</sendScannedSlideImageLabelId>";
    }
}
