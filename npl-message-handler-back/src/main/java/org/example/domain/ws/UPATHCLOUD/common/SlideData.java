package org.example.domain.ws.UPATHCLOUD.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class SlideData extends WSSegment {
    private String imageUrl;
    private String slideLabelId;
    private String slideViewUrl;
    private String status;
    private String thumbnailImage;

    public static SlideData FromSlide(Slide slide) {
        SlideData slideData = new SlideData();

        slideData.imageUrl = "http://hostname:85/88.bif";
        slideData.slideLabelId = slide.getId();
        slideData.slideViewUrl = "http://hostname/virtuoso/slideview";
        slideData.status = "RELEASED";
        slideData.thumbnailImage = "/9j/4AAQSkZJRgABAQEAYA";

        return  slideData;
    }

    public boolean isEmpty() {
        return Stream.of(slideLabelId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String slideData = addIndentation(indentationLevel) + "<allSlideData>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            slideData += addIndentation(indentationLevel) + "<imageUrl>" +  nullSafe(imageUrl) + "</imageUrl>\n"
                    + addIndentation(indentationLevel) + "<slideLabelId>" +  nullSafe(slideLabelId) + "</slideLabelId>\n"
                    + addIndentation(indentationLevel) + "<slideViewUrl>" +  nullSafe(slideViewUrl) + "</slideViewUrl>\n"
                    + addIndentation(indentationLevel) + "<status>" +  nullSafe(status) + "</status>\n"
                    + addIndentation(indentationLevel) + "<thumbnailImage>" +  nullSafe(thumbnailImage) + "</thumbnailImage>\n";

            indentationLevel --;
        }

        slideData += addIndentation(indentationLevel) + "</allSlideData>";
        return slideData;
    }
}
