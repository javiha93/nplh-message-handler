package org.example.server.impl;

import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.ACK;
import org.example.domain.host.host.Connection;
import org.example.server.HL7Server;
import org.example.service.IrisService;
import org.example.utils.HL7LLPCharacters;
import org.example.utils.MessageLogger;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LISHandler extends HL7Server {

    static final Logger logger = LoggerFactory.getLogger(LISHandler.class);
    private final MessageLogger messageLogger;

    public LISHandler(String hostName, Connection connection, IrisService irisService) {
        super(hostName, connection, irisService);
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + hostName), irisService, hostName, MockType.SERVER);
        this.applicationResponse = true;
        this.communicationResponse = true;
    }

    @Override
    protected void response(OutputStream outputStream, String receivedMessage) {
        List<String> responses = new ArrayList<>();

        if (communicationResponse) {
            ACK ack = ACK.CommunicationOK(extractUUID(receivedMessage));
            sendResponse(outputStream, formatHL7Response(ack.toString()));

            responses.add(ack.toString());
        }
        if (applicationResponse) {
            ACK ack = ACK.ApplicationOK(extractUUID(receivedMessage));
            sendResponse(outputStream, formatHL7Response(ack.toString()));

            responses.add(ack.toString());
        }

        // Registrar la respuesta
        messageLogger.addServerMessage("", receivedMessage, responses);
    }

    private String formatHL7Response(String ack) {
        return HL7LLPCharacters.VT.getCharacter() +
                ack.replace('\n', HL7LLPCharacters.CR.getCharacter()) +
                HL7LLPCharacters.FS.getCharacter() +
                HL7LLPCharacters.CR.getCharacter();
    }

    private void sendResponse(OutputStream outputStream, String response) {
        try {
            outputStream.write(response.getBytes());
            outputStream.flush();

            logger.info("Sent response: {}", response);

            // Registrar la respuesta
//            List<String> responses = new ArrayList<>();
//            responses.add(responseText);
//            messageLogger.addServerMessage("", receivedMessage, responses);

        } catch (IOException e) {
            logger.error("Error sending response", e);
        }
    }
}
