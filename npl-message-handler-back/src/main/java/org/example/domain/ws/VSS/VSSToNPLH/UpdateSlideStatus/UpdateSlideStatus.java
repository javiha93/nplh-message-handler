package org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class UpdateSlideStatus extends WSSegment {
    private SlideStatusInfo slideStatusInfo;

    public static UpdateSlideStatus FromSlide(Message message, Slide slide, String status) {
        UpdateSlideStatus updateSlideStatus = new UpdateSlideStatus();

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
