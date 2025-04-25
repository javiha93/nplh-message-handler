package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Order;
import org.example.domain.ws.VTGWS.WSSegment;

@Data
@NoArgsConstructor
public class StainOrder extends WSSegment {
    private Block block;
    private String receivedDateTime;
    private Slide slide;
    private Specimen specimen;

    public static StainOrder FromOrder(Message message, org.example.domain.message.entity.Slide entitySlide) {
        StainOrder stainOrder = new StainOrder();

        org.example.domain.message.entity.Block entityBlock = entitySlide.getBlockParent(message);
        org.example.domain.message.entity.Specimen entitySpecimen = entityBlock.getSpecimenParent(message);

        stainOrder.setBlock(Block.Default(entityBlock, entitySpecimen));
        stainOrder.setSlide(Slide.Default(entitySlide));
        stainOrder.setSpecimen(Specimen.Default(entitySpecimen));
        stainOrder.setReceivedDateTime(entitySpecimen.getReceivedDateTime());

        return stainOrder;
    }

    private boolean isEmpty() {
        return (block == null || block.isEmpty())
                || (slide == null || slide.isEmpty())
                ||(specimen == null || specimen.isEmpty());
    }

    public String toString(int indentationLevel) {
        String labOrder = addIndentation(indentationLevel) + "<StainOrder>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            labOrder +=  nullSafe(block, Block::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<ReceivedDateTime>" +  nullSafe(receivedDateTime) + "</ReceivedDateTime>\n"
                    + nullSafe(slide, Slide::new).toString(indentationLevel) + "\n"
                    + nullSafe(specimen, Specimen::new).toString(indentationLevel) + "\n";

            indentationLevel --;
        }

        labOrder += addIndentation(indentationLevel) + "</StainOrder>";
        return labOrder;
    }
}
