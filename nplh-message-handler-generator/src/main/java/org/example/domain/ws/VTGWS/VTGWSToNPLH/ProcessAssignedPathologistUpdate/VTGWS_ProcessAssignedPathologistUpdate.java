package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessAssignedPathologistUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.Pathologist;

@Data
@NoArgsConstructor
public class VTGWS_ProcessAssignedPathologistUpdate extends WSSegment {
    private String caseId;
    private Pathologist pathologist;
    private String transactionId;

    public static VTGWS_ProcessAssignedPathologistUpdate FromMessage(Message message) {
        VTGWS_ProcessAssignedPathologistUpdate processAssignedPathologistUpdate = new VTGWS_ProcessAssignedPathologistUpdate();

        processAssignedPathologistUpdate.setCaseId(message.getOrder().getSampleId());
        processAssignedPathologistUpdate.setPathologist(Pathologist.FromPathologist(message.getOrder().getPathologist()));
        processAssignedPathologistUpdate.setTransactionId(message.getHeader().getMessageControlId());

        return processAssignedPathologistUpdate;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessAssignedPathologistUpdate>\n" +
                addIndentation(indentationLevel) + "<caseId>" + caseId + "</caseId>\n" +
                pathologist.toString(indentationLevel, "assignedPathologist") + "\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                "</ProcessAssignedPathologistUpdate>";
    }
}
