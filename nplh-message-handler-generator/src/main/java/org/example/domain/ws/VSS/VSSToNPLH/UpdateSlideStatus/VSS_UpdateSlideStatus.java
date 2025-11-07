package org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class VSS_UpdateSlideStatus extends WSSegment {
    private SlideStatusInfo slideStatusInfo;

    public static VSS_UpdateSlideStatus FromSlide(Message message, Slide slide, String status) {
        VSS_UpdateSlideStatus updateSlideStatus = new VSS_UpdateSlideStatus();

        updateSlideStatus.slideStatusInfo = SlideStatusInfo.FromSlide(message, slide, status);

        return updateSlideStatus;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<UpdateSlideStatus>\n" +
                addIndentation(indentationLevel) + slideStatusInfo.toString(indentationLevel + 1) + "\n" +
                "</UpdateSlideStatus>";
    }
}
