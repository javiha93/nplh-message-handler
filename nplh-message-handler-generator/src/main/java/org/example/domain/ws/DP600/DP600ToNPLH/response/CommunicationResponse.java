package org.example.domain.ws.DP600.DP600ToNPLH.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class CommunicationResponse extends WSSegment {

    private Boolean status;
    private String soapAction;
    private String errorCode;
    private String errorText;

    public static CommunicationResponse FromSoapActionOk(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setStatus(true);

        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction, String errorText) {
        CommunicationResponse communicationResponse = FromSoapActionError(soapAction);

        communicationResponse.setErrorCode("9999");
        communicationResponse.setErrorText(errorText);


        return communicationResponse;
    }

    public static CommunicationResponse FromSoapActionError(String soapAction) {
       CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setStatus(false);

        return communicationResponse;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<" + soapAction + "Response>\n" +
                addIndentation(indentationLevel) + "<return>\n" +
                addIndentation(indentationLevel + 1) + "<errorCode>" + nullSafe(errorCode) + "</errorCode>\n" +
                addIndentation(indentationLevel + 1) + "<errorMessage>" + nullSafe(errorText) + "</errorMessage>\n" +
                addIndentation(indentationLevel + 1) + "<succeed>" + status + "</succeed>\n" +
                addIndentation(indentationLevel) + "</return>\n" +
                "</" + soapAction + "Response>";
    }
}
