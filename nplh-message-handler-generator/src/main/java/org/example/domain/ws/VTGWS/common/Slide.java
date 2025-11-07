package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class Slide extends WSSegment {

    private String barcode;
    private String sequence;
    private String slideComments;
    private StainProtocol stainProtocol;
    private String techConsultId;
    public static Slide Default(org.example.domain.message.entity.Slide entitySlide) {
        Slide slide = new Slide();

        slide.barcode = entitySlide.getId();
        slide.sequence = entitySlide.getSequence();
        slide.stainProtocol = StainProtocol.Default(entitySlide.getStainProtocol());

        return slide;
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, slideComments, techConsultId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Slide>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + nullSafe(stainProtocol, StainProtocol::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<SlideComments>" +  nullSafe(slideComments) + "</SlideComments>\n"
                    + addIndentation(indentationLevel) + "<TechConsultId>" +  nullSafe(techConsultId) + "</TechConsultId>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Slide>";
        return specimen;
    }

}
