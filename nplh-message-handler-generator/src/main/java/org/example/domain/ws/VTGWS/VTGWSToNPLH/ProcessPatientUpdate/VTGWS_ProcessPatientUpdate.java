package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPatientUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;
import org.example.domain.ws.VTGWS.common.Patient;

@Data
@NoArgsConstructor
public class VTGWS_ProcessPatientUpdate extends WSSegment {
    private String caseId;
    private Patient patient;
    private String transactionId;

    public static VTGWS_ProcessPatientUpdate FromMessage(Message message) {
        VTGWS_ProcessPatientUpdate processPatientUpdate = new VTGWS_ProcessPatientUpdate();

        processPatientUpdate.setCaseId(message.getOrder().getSampleId());
        processPatientUpdate.setPatient(Patient.FromPatient(message.getPatient()));
        processPatientUpdate.setTransactionId(message.getHeader().getMessageControlId());

        return processPatientUpdate;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessPatientUpdate>\n" +
                addIndentation(indentationLevel) + "<caseId>" + caseId + "</caseId>\n" +
                addIndentation(indentationLevel) + patient.toString(indentationLevel) + "\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                "</ProcessPatientUpdate>";
    }

}
