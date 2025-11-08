package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class Slide extends WSSegment {
    @JacksonXmlProperty(localName = "Barcode")
    private String barcode;
    @JacksonXmlProperty(localName = "Sequence")
    private String sequence;
    @JacksonXmlProperty(localName = "SlideComments")
    private String slideComments;
    @JacksonXmlProperty(localName = "StainProtocol")
    private StainProtocol stainProtocol;
    @JacksonXmlProperty(localName = "TechConsultId")
    private String techConsultId;
    public static Slide Default(org.example.domain.message.entity.Slide entitySlide) {
        Slide slide = new Slide();

        slide.barcode = entitySlide.getId();
        slide.sequence = entitySlide.getSequence();
        slide.stainProtocol = StainProtocol.Default(entitySlide.getStainProtocol());

        return slide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Slide slide = (Slide) o;

        return Objects.equals(barcode, slide.barcode)
                && Objects.equals(sequence, slide.sequence)
                && Objects.equals(slideComments, slide.slideComments)
                && Objects.equals(stainProtocol, slide.stainProtocol)
                && Objects.equals(techConsultId, slide.techConsultId);
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
