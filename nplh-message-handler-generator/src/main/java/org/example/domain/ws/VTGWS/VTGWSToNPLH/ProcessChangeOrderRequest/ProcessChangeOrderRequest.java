package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrderRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class ProcessChangeOrderRequest extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static ProcessChangeOrderRequest FromMessage(Message message) {
        ProcessChangeOrderRequest processChangeOrderRequest = new ProcessChangeOrderRequest();

        processChangeOrderRequest.setTransactionId(message.getHeader().getMessageControlId());
        processChangeOrderRequest.setLabOrder(LabOrder.FromMessage(message));

        return processChangeOrderRequest;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessChangeOrderRequest>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessChangeOrderRequest>";
    }
}
