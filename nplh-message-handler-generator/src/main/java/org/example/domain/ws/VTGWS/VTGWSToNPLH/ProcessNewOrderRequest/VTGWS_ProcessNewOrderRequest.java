package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrderRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class VTGWS_ProcessNewOrderRequest extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static VTGWS_ProcessNewOrderRequest FromMessage(Message message) {
        VTGWS_ProcessNewOrderRequest processNewOrderRequest = new VTGWS_ProcessNewOrderRequest();

        processNewOrderRequest.setTransactionId(message.getHeader().getMessageControlId());
        processNewOrderRequest.setLabOrder(LabOrder.FromMessage(message));

        return processNewOrderRequest;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessNewOrderRequest>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessNewOrderRequest>";
    }
}
