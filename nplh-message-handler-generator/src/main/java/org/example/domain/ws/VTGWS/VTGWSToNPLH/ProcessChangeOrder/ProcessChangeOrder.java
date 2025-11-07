package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class ProcessChangeOrder extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static ProcessChangeOrder FromMessage(Message message) {
        ProcessChangeOrder processChangeOrder = new ProcessChangeOrder();

        processChangeOrder.setTransactionId(message.getHeader().getMessageControlId());
        processChangeOrder.setLabOrder(LabOrder.FromMessage(message));

        return processChangeOrder;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessChangeOrder>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                labOrder.toString(indentationLevel) + "\n" +
                "</ProcessChangeOrder>";
    }
}
