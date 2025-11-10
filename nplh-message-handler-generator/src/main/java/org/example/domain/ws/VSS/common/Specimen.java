package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Specimen extends WSSegment {
    @JacksonXmlProperty(localName = "Barcode")
    private String barcode;
    @JacksonXmlProperty(localName = "Sequence")
    private String sequence;
    @JacksonXmlProperty(localName = "TechConsultId")
    private String techConsultId;
    @JacksonXmlProperty(localName = "HumanReadableId")
    private String humanReadableId;

    public static Specimen FromSpecimen(org.example.domain.message.entity.Specimen entitySpecimen) {
        Specimen specimen = new Specimen();

        specimen.barcode = entitySpecimen.getId();
        specimen.sequence = entitySpecimen.getSequence();
        specimen.techConsultId = entitySpecimen.getExternalId();
        //TODO check logic specimen.humanReadableId;

        return specimen;
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, techConsultId, humanReadableId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specimen specimen = (Specimen) o;

        return Objects.equals(barcode, specimen.barcode)
                && Objects.equals(sequence, specimen.sequence)
                && Objects.equals(techConsultId, specimen.techConsultId)
                && Objects.equals(humanReadableId, specimen.humanReadableId);
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Specimen>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<TechConsultSlideId>" +  nullSafe(techConsultId) + "</TechConsultSlideId>\n"
                    + addIndentation(indentationLevel) + "<HumanReadableId>" +  nullSafe(humanReadableId) + "</HumanReadableId>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Specimen>";
        return specimen;
    }
}
