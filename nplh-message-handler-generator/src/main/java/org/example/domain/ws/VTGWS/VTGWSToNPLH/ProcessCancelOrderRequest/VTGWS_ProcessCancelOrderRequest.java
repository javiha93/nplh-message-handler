package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrderRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class VTGWS_ProcessCancelOrderRequest extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static VTGWS_ProcessCancelOrderRequest FromMessage(Message message) {
        VTGWS_ProcessCancelOrderRequest processCancelOrderRequest = new VTGWS_ProcessCancelOrderRequest();

        processCancelOrderRequest.setTransactionId(message.getHeader().getMessageControlId());
        processCancelOrderRequest.setLabOrder(LabOrder.FromMessage(message));

        return processCancelOrderRequest;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessCancelOrderRequest>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessCancelOrderRequest>";
    }
}
