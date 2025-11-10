package org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessCancelOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class VTGWS_ProcessCancelOrder extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static VTGWS_ProcessCancelOrder FromMessage(Message message) {
        VTGWS_ProcessCancelOrder processCancelOrder = new VTGWS_ProcessCancelOrder();

        processCancelOrder.setTransactionId(message.getHeader().getMessageControlId());
        processCancelOrder.setLabOrder(LabOrder.fromMessage(message));

        return processCancelOrder;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessCancelOrder>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessCancelOrder>";
    }
}
