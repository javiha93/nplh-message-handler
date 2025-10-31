package org.example.domain.ws.VTGWS.VTGWSToNPLH.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ProcessApplicationACK extends WSSegment {
    private String errorCode;
    private String errorDescription;
    private Boolean status;
    private String originalTransactionId;
    private String transactionId;

    public static ProcessApplicationACK FromOriginalTransactionIdOk(String originalTransactionId) {
        ProcessApplicationACK processApplicationACK = new ProcessApplicationACK();

        processApplicationACK.setOriginalTransactionId(originalTransactionId);
        processApplicationACK.setTransactionId(UUID.randomUUID().toString());
        processApplicationACK.setStatus(true);

        return processApplicationACK;
    }

    public static ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId, String errorCode, String errorDescription) {
        ProcessApplicationACK processApplicationACK = FromOriginalTransactionIdError(originalTransactionId, errorDescription);
        processApplicationACK.setErrorCode(errorCode);

        return processApplicationACK;
    }

    public static ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId, String errorDescription) {
        ProcessApplicationACK processApplicationACK = FromOriginalTransactionIdError(originalTransactionId);
        processApplicationACK.setErrorDescription(errorDescription);

        return processApplicationACK;
    }

    public static ProcessApplicationACK FromOriginalTransactionIdError(String originalTransactionId) {
        ProcessApplicationACK processApplicationACK = new ProcessApplicationACK();

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
