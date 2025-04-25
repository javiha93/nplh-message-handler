package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrderRequest;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPatientUpdate.ProcessPatientUpdate;
import org.example.domain.ws.VTGWS.WSSegment;
import org.example.domain.ws.VTGWS.common.LabOrder;
import org.example.domain.ws.VTGWS.common.Patient;

@Data
@NoArgsConstructor
public class ProcessNewOrderRequest extends WSSegment {

    private String transactionId;
    private LabOrder labOrder;

    public static ProcessNewOrderRequest FromMessage(Message message) {
        ProcessNewOrderRequest processNewOrderRequest = new ProcessNewOrderRequest();

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
