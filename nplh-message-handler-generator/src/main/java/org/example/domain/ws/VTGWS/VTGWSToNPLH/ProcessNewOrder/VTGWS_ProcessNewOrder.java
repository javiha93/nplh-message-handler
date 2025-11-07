package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class VTGWS_ProcessNewOrder extends WSSegment {
    private String transactionId;
    private LabOrder labOrder;

    public static VTGWS_ProcessNewOrder FromMessage(Message message) {
        VTGWS_ProcessNewOrder processNewOrder = new VTGWS_ProcessNewOrder();

        processNewOrder.setTransactionId(message.getHeader().getMessageControlId());
        processNewOrder.setLabOrder(LabOrder.FromMessage(message));

        return processNewOrder;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessNewOrder>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessNewOrder>";
    }
}
