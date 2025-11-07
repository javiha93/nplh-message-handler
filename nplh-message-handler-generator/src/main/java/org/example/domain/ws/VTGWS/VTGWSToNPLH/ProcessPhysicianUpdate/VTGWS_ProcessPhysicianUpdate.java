package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPhysicianUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.Physician;

@Data
@NoArgsConstructor
public class VTGWS_ProcessPhysicianUpdate extends WSSegment {
    private String caseId;
    private Physician physician;
    private String transactionId;

    public static VTGWS_ProcessPhysicianUpdate FromMessage(Message message) {
        VTGWS_ProcessPhysicianUpdate processPhysicianUpdate = new VTGWS_ProcessPhysicianUpdate();

        processPhysicianUpdate.setCaseId(message.getOrder().getSampleId());
        processPhysicianUpdate.setPhysician(Physician.FromPhysician(message.getPhysician()));
        processPhysicianUpdate.setTransactionId(message.getHeader().getMessageControlId());

        return processPhysicianUpdate;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessPhysicianUpdate>\n" +
                addIndentation(indentationLevel) + "<caseId>" + caseId + "</caseId>\n" +
                addIndentation(indentationLevel) + physician.toString(indentationLevel, "requestingPhysician") + "\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                "</ProcessPhysicianUpdate>";
    }
}
