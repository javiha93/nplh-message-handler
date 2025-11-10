package org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessChangeOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;

@Data
@NoArgsConstructor
public class VTGWS_ProcessChangeOrder extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static VTGWS_ProcessChangeOrder FromMessage(Message message) {
        VTGWS_ProcessChangeOrder processChangeOrder = new VTGWS_ProcessChangeOrder();

        processChangeOrder.setTransactionId(message.getHeader().getMessageControlId());
        processChangeOrder.setLabOrder(LabOrder.fromMessage(message));

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
