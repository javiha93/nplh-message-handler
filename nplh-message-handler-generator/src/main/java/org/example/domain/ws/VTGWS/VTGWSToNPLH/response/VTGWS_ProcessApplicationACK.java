package org.example.domain.ws.VTGWS.VTGWSToNPLH.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.UUID;

@Data
@NoArgsConstructor
public class VTGWS_ProcessApplicationACK extends WSSegment {
    private String errorCode;
    private String errorDescription;
    private Boolean status;
    private String originalTransactionId;
    private String transactionId;

    public static VTGWS_ProcessApplicationACK FromOriginalTransactionIdOk(String originalTransactionId) {
        VTGWS_ProcessApplicationACK processApplicationACK = new VTGWS_ProcessApplicationACK();

        processApplicationACK.setOriginalTransactionId(originalTransactionId);
        processApplicationACK.setTransactionId(UUID.randomUUID().toString());
        processApplicationACK.setStatus(true);

        return processApplicationACK;
    }

    public static VTGWS_ProcessApplicationACK FromOriginalTransactionIdOk(String originalTransactionId, String controlId) {
        VTGWS_ProcessApplicationACK processApplicationACK = new VTGWS_ProcessApplicationACK();

        processApplicationACK.setOriginalTransactionId(originalTransactionId);
        processApplicationACK.setTransactionId(controlId);
        processApplicationACK.setStatus(true);

        return processApplicationACK;
    }

    public static VTGWS_ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId, String errorCode, String errorDescription) {
        VTGWS_ProcessApplicationACK processApplicationACK = FromOriginalTransactionIdError(originalTransactionId, errorDescription);
        processApplicationACK.setErrorCode(errorCode);

        return processApplicationACK;
    }

    public static VTGWS_ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId, String errorDescription) {
        VTGWS_ProcessApplicationACK processApplicationACK = FromOriginalTransactionIdError(originalTransactionId);
        processApplicationACK.setErrorDescription(errorDescription);

        return processApplicationACK;
    }

    public static VTGWS_ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId) {
        VTGWS_ProcessApplicationACK processApplicationACK = new VTGWS_ProcessApplicationACK();

        processApplicationACK.setOriginalTransactionId(originalTransactionId);
        processApplicationACK.setTransactionId(UUID.randomUUID().toString());
        processApplicationACK.setStatus(false);

        return processApplicationACK;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessApplicationACK>\n" +
                addIndentation(indentationLevel) + "<applicationACK>\n" +
                addIndentation(indentationLevel + 1) + "<ErrorCode>" + nullSafe(errorCode) + "</ErrorCode>\n" +
                addIndentation(indentationLevel + 1) + "<ErrorDescription>" + nullSafe(errorDescription) + "</ErrorDescription>\n" +
                addIndentation(indentationLevel + 1) + "<IsSuccessful>" + status + "</IsSuccessful>\n" +
                addIndentation(indentationLevel + 1) + "<OriginalTransactionId>" + originalTransactionId + "</OriginalTransactionId>\n" +
                addIndentation(indentationLevel) + "</applicationACK>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                "</ProcessApplicationACK>";
    }
}
