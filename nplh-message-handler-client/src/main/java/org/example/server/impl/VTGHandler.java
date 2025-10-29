package org.example.server.impl;

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
import java.util.UUID;

public class VTGHandler extends HL7Server {
    static final Logger logger = LoggerFactory.getLogger(LISHandler.class);
    private final MessageLogger messageLogger;

    public VTGHandler(String hostName, Connection connection, IrisService irisService) {
        super(hostName, connection, irisService);
        this.applicationResponse = true;
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + hostName), irisService, hostName, MockType.SERVER);
    }

    @Override
    protected void response(OutputStream outputStream, String receivedMessage) {
        try {
            String responseText = "MSH|^~\\&|LIS|XYZ Laboratory|Ventana|ABC Laboratory|20251015150037||ACK|"+ UUID.randomUUID() + "|P|2.4" + HL7LLPCharacters.CR.getCharacter() +
                    "MSA|AA|" + extractUUID(receivedMessage);

            String fullResponse = HL7LLPCharacters.VT.getCharacter() +
                    responseText +
                    HL7LLPCharacters.FS.getCharacter() +
                    HL7LLPCharacters.CR.getCharacter();

            outputStream.write(fullResponse.getBytes());
            outputStream.flush();

            logger.info("Sent response: {}", responseText);

            // Registrar la respuesta
            List<String> responses = new ArrayList<>();
            responses.add(responseText);
            messageLogger.addServerMessage("", receivedMessage, responses);

        } catch (IOException e) {
            logger.error("Error sending response", e);
        }
    }

}
