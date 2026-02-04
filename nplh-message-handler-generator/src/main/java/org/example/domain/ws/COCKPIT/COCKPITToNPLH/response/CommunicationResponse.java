package org.example.domain.ws.COCKPIT.COCKPITToNPLH.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class CommunicationResponse extends WSSegment {

    private Boolean success;
    private String soapAction;
    private String statusCode;
    private String statusMessage;

    public static CommunicationResponse FromSoapActionOk(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setSuccess(true);

        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction, String errorText) {
        CommunicationResponse communicationResponse = FromSoapActionError(soapAction);

        communicationResponse.setStatusCode("9999");
        communicationResponse.setStatusMessage(errorText);


        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setSuccess(false);

        return communicationResponse;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<" + soapAction + "Response>\n" +
                addIndentation(indentationLevel) + "<return>\n" +
                addIndentation(indentationLevel + 1) + "<statusCode>" + nullSafe(statusCode) + "</statusCode>\n" +
                addIndentation(indentationLevel + 1) + "<statusMessage>" + nullSafe(statusMessage) + "</statusMessage>\n" +
                addIndentation(indentationLevel + 1) + "<success>" + success + "</success>\n" +
                addIndentation(indentationLevel) + "</return>\n" +
                "</" + soapAction + "Response>";
    }
}
