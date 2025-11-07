package org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendScannedSlide;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class SendScannedSlide extends WSSegment {
    private String slideId;

    public static SendScannedSlide FromMessage(Slide slide) {
        SendScannedSlide sendScannedSlide = new SendScannedSlide();

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
