package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Slide extends WSSegment {
    @JacksonXmlProperty(localName = "Barcode")
    private String barcode;
    @JacksonXmlProperty(localName = "Sequence")
    private String sequence;
    @JacksonXmlProperty(localName = "TechConsultId")
    private String techConsultId;
    @JacksonXmlProperty(localName = "HumanReadableId")
    private String humanReadableId;

    public static Slide FromSlide(org.example.domain.message.entity.Slide entitySlide) {
        Slide slide = new Slide();

        slide.barcode = entitySlide.getId();
        slide.sequence = entitySlide.getSequence();
        slide.techConsultId = entitySlide.getExternalId();
        //TODO check logic slide.humanReadableId;

        return slide;
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, techConsultId, humanReadableId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Slide slide = (Slide) o;

        return Objects.equals(barcode, slide.barcode)
                && Objects.equals(sequence, slide.sequence)
                && Objects.equals(techConsultId, slide.techConsultId)
                && Objects.equals(humanReadableId, slide.humanReadableId);
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
