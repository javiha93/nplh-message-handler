package org.example.domain.ws.VTGWS.NPLHTpVTGWS.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class CommunicationResponse extends WSSegment {
    private Boolean status;
    private String soapAction;

    public static CommunicationResponse FromSoapActionOk(String soapAction) {
        CommunicationResponse communicationResponse = new CommunicationResponse();

        communicationResponse.setSoapAction(soapAction);
        communicationResponse.setStatus(true);

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
                addIndentation(indentationLevel) + "<" + soapAction + "Result>\n" +
                addIndentation(indentationLevel + 1) + "<IsSuccessful>" + status + "</IsSuccessful>\n" +
                addIndentation(indentationLevel) + "</" + soapAction + "Result>\n" +
                "</" + soapAction + "Response>";
    }
}
