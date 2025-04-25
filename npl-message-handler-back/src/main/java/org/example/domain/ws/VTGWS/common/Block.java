package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.VTGWS.WSSegment;

import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Block extends WSSegment {

    private String barcode;
    private String sequence;
    private String tissueSubTypeDescription;
    private String tissueSubTypeName;

    public static Block Default(org.example.domain.message.entity.Block entityBlock, Specimen specimen) {
        Block block = new Block();

        block.barcode = entityBlock.getId();
        block.sequence = entityBlock.getSequence();
        block.tissueSubTypeDescription = specimen.getProcedure().getTissue().getSubtypeDescription();
        block.tissueSubTypeName = specimen.getProcedure().getTissue().getType();

        return  block;
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

    @Override
    public String toString() {
        String result = "<Block>\n";

        if (barcode != null) {
            result += "            <Barcode>" + barcode + "</Barcode>\n";
        }

        if (sequence != null) {
            result += "            <Sequence>" + sequence + "</Sequence>\n";
        }

        if (tissueSubTypeDescription != null) {
            result += "            <TissueSubTypeDescription>" + tissueSubTypeDescription + "</TissueSubTypeDescription>\n";
        }

        if (tissueSubTypeName != null) {
            result += "            <TissueSubTypeName>" + tissueSubTypeName + "</TissueSubTypeName>\n";
        }

        result += "        </Block>";
        return result;
    }
}
