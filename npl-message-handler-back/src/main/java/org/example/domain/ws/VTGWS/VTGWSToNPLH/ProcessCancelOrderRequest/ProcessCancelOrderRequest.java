package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrderRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.VTGWS.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class ProcessCancelOrderRequest extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static ProcessCancelOrderRequest FromMessage(Message message) {
        ProcessCancelOrderRequest processCancelOrderRequest = new ProcessCancelOrderRequest();

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
