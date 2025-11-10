package org.example.domain.ws.VSS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Block extends WSSegment {
    @JacksonXmlProperty(localName = "Barcode")
    private String barcode;
    @JacksonXmlProperty(localName = "Sequence")
    private String sequence;
    @JacksonXmlProperty(localName = "TechConsultId")
    private String techConsultId;
    @JacksonXmlProperty(localName = "HumanReadableId")
    private String humanReadableId;

    public static Block FromBlock(org.example.domain.message.entity.Block entityBlock) {
        Block block = new Block();

        block.barcode = entityBlock.getId();
        block.sequence = entityBlock.getSequence();
        block.techConsultId = entityBlock.getExternalId();
        //TODO check logic slide.humanReadableId; block.humanReadableId;

        return block;
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, techConsultId, humanReadableId)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return Objects.equals(barcode, block.barcode)
                && Objects.equals(sequence, block.sequence)
                && Objects.equals(techConsultId, block.techConsultId)
                && Objects.equals(humanReadableId, block.humanReadableId);
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Block>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<TechConsultSlideId>" +  nullSafe(techConsultId) + "</TechConsultSlideId>\n"
                    + addIndentation(indentationLevel) + "<HumanReadableId>" +  nullSafe(humanReadableId) + "</HumanReadableId>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Block>";
        return specimen;
    }

    public String toStringTube(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Tube>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<TechConsultSlideId>" +  nullSafe(techConsultId) + "</TechConsultSlideId>\n"
                    + addIndentation(indentationLevel) + "<HumanReadableId>" +  nullSafe(humanReadableId) + "</HumanReadableId>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Tube>";
        return specimen;
    }
}
