package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class ProcessCancelOrder extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static ProcessCancelOrder FromMessage(Message message) {
        ProcessCancelOrder processCancelOrder = new ProcessCancelOrder();

        processCancelOrder.setTransactionId(message.getHeader().getMessageControlId());
        processCancelOrder.setLabOrder(LabOrder.FromMessage(message));

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
