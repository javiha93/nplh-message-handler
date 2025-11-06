package org.example.server.impl;

import org.example.domain.CustomResponse;
import org.example.domain.ResponseInfo;
import org.example.domain.ResponseStatus;
import org.example.domain.hl7.VTG.VTGToNPLH.response.ACK.ACK;
import org.example.domain.host.host.Connection;
import org.example.server.HL7Server;
import org.example.service.IrisService;
import org.example.utils.MessageLogger;
import org.example.utils.MockType;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VTGHandler extends HL7Server {
    private final MessageLogger messageLogger;

    public VTGHandler(String hostName, Connection connection, IrisService irisService) {
        super(hostName, connection, irisService);
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + hostName), irisService, hostName, MockType.SERVER);

        ResponseStatus applicationResponse = ResponseStatus.enabled();
        CustomResponse customResponse = CustomResponse.disabled(ACK.ApplicationOK("*originalControlId*", "*controlId*").toString());
        applicationResponse.setCustomResponse(customResponse);

        ResponseStatus communicationResponse = ResponseStatus.disabled();
        customResponse = CustomResponse.disabled(ACK.CommunicationOK("*originalControlId*", "*controlId*").toString());
        communicationResponse.setCustomResponse(customResponse);

        setDefaultResponse(ResponseInfo.createDefault(applicationResponse, communicationResponse));
    }

    @Override
    protected List<String> response(OutputStream outputStream, String receivedMessage) {
        List<String> responses = new ArrayList<>();

        ResponseStatus communicationResponse = this.getDefaultResponse().getCommunicationResponse();
        ResponseStatus applicationResponse = this.getDefaultResponse().getApplicationResponse();

        if (communicationResponse.getIsEnable()) {
            String ack;

            if (communicationResponse.getCustomResponse().getUseCustomResponse()) {
                ack = communicationResponse.getCustomResponse().getCustomResponseText();
                ack = ack.replace("*originalControlId*", extractUUID(receivedMessage));
                ack = ack.replace("*controlId*", UUID.randomUUID().toString());
            } else if (communicationResponse.getIsError()) {
                ack = ACK.CommunicationError(extractUUID(receivedMessage), communicationResponse.getErrorText()).toString();
            } else {
                ack = ACK.CommunicationOK(extractUUID(receivedMessage)).toString();
            }
            sendResponse(outputStream, formatHL7Response(ack));

            responses.add(ack);
        }
        if (applicationResponse.getIsEnable()) {
            String ack;
            if (applicationResponse.getCustomResponse().getUseCustomResponse()) {
                ack = applicationResponse.getCustomResponse().getCustomResponseText();
                ack = ack.replace("*originalControlId*", extractUUID(receivedMessage));
                ack = ack.replace("*controlId*", UUID.randomUUID().toString());
            } else if (applicationResponse.getIsError()) {
                ack = ACK.ApplicationError(extractUUID(receivedMessage), applicationResponse.getErrorText()).toString();
            } else {
                ack = ACK.ApplicationOK(extractUUID(receivedMessage)).toString();
            }
            sendResponse(outputStream, formatHL7Response(ack));

            responses.add(ack);
        }

        messageLogger.addServerMessage("", receivedMessage, responses);
        return responses;
    }

}
