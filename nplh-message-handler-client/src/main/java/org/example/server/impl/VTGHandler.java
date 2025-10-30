package org.example.server.impl;

import org.example.domain.ResponseStatus;
import org.example.domain.hl7.VTG.VTGToNPLH.response.ACK.ACK;
import org.example.domain.host.host.Connection;
import org.example.server.HL7Server;
import org.example.service.IrisService;
import org.example.utils.MessageLogger;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class VTGHandler extends HL7Server {
    static final Logger logger = LoggerFactory.getLogger(VTGHandler.class);
    private final MessageLogger messageLogger;

    public VTGHandler(String hostName, Connection connection, IrisService irisService) {
        super(hostName, connection, irisService);
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + hostName), irisService, hostName, MockType.SERVER);

        this.applicationResponse = ResponseStatus.enabledWithError("ErrorDePrueba");
        this.communicationResponse = ResponseStatus.disabled();
    }

    @Override
    protected void response(OutputStream outputStream, String receivedMessage) {
        List<String> responses = new ArrayList<>();

        if (communicationResponse.getIsEnable()) {
            ACK ack;
            if (communicationResponse.getIsError()) {
                ack = ACK.CommunicationError(extractUUID(receivedMessage), communicationResponse.getErrorText());
            } else {
                ack = ACK.CommunicationOK(extractUUID(receivedMessage));
            }
            sendResponse(outputStream, formatHL7Response(ack.toString()));

            responses.add(ack.toString());
        }
        if (applicationResponse.getIsEnable()) {
            ACK ack;
            if (applicationResponse.getIsError()) {
                ack = ACK.ApplicationError(extractUUID(receivedMessage), applicationResponse.getErrorText());
            } else {
                ack = ACK.ApplicationOK(extractUUID(receivedMessage));
            }
            sendResponse(outputStream, formatHL7Response(ack.toString()));

            responses.add(ack.toString());
        }

        // Registrar la respuesta
        messageLogger.addServerMessage("", receivedMessage, responses);
    }

}
