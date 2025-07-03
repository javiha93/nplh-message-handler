package org.example.domain.ws.VSS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Slide extends WSSegment {
    private String barcode;
    private String sequence;
    private String techConsultId;
    private String humanReadableId;

    public static Slide FromSlide(org.example.domain.message.entity.Slide entitySlide) {
        Slide slide = new Slide();

        slide.barcode = entitySlide.getId();
        slide.sequence = entitySlide.getSequence();
        slide.techConsultId = entitySlide.getId();
        slide.humanReadableId = entitySlide.getId();

        return slide;
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, techConsultId, humanReadableId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Slide>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<TechConsultSlideId>" +  nullSafe(techConsultId) + "</TechConsultSlideId>\n"
                    + addIndentation(indentationLevel) + "<HumanReadableId>" +  nullSafe(humanReadableId) + "</HumanReadableId>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Slide>";
        return specimen;
    }

}
