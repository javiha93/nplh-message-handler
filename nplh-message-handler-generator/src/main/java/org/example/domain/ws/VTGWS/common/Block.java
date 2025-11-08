package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Specimen;
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
    @JacksonXmlProperty(localName = "TissueSubTypeDescription")
    private String tissueSubTypeDescription;
    @JacksonXmlProperty(localName = "TissueSubTypeName")
    private String tissueSubTypeName;

    public static Block Default(org.example.domain.message.entity.Block entityBlock, Specimen specimen) {
        Block block = new Block();

        block.barcode = entityBlock.getId();
        block.sequence = entityBlock.getSequence();
        block.tissueSubTypeDescription = specimen.getProcedure().getTissue().getSubtypeDescription();
        block.tissueSubTypeName = specimen.getProcedure().getTissue().getSubtype();

        return  block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        return Objects.equals(barcode, block.barcode)
                && Objects.equals(sequence, block.sequence)
                && Objects.equals(tissueSubTypeDescription, block.tissueSubTypeDescription)
                && Objects.equals(tissueSubTypeName, block.tissueSubTypeName);
    }

    public boolean isEmpty() {
        return Stream.of(barcode, sequence, tissueSubTypeDescription, tissueSubTypeName)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<Block>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Barcode>" +  nullSafe(barcode) + "</Barcode>\n"
                    + addIndentation(indentationLevel) + "<Sequence>" +  nullSafe(sequence) + "</Sequence>\n"
                    + addIndentation(indentationLevel) + "<TissueSubTypeDescription>" +  nullSafe(tissueSubTypeDescription) + "</TissueSubTypeDescription>\n"
                    + addIndentation(indentationLevel) + "<TissueSubTypeName>" +  nullSafe(tissueSubTypeName) + "</TissueSubTypeName>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</Block>";
        return specimen;
    }

}
