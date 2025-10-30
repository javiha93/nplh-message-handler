package org.example.server;

import org.example.domain.host.HL7Host;
import org.example.domain.host.host.Connection;
import org.example.service.IrisService;
import org.example.utils.HL7LLPCharacters;
import org.example.utils.MessageLogger;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.example.utils.MessageHandler.llpToText;

public class HL7Server extends Server implements Runnable {
    static final Logger logger = LoggerFactory.getLogger(HL7Server.class);
    private final MessageLogger messageLogger;

    IrisService irisService;
    ServerSocket serverSocket;

    public HL7Server(String hostName, Connection connection, IrisService irisService) {
        try {
            serverName = hostName;
            serverSocket = new ServerSocket(connection.getPort());
            serverSocket.setSoTimeout(1000);
            this.isRunning = true;
            this.irisService = irisService;
            this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), irisService, this.serverName, MockType.SERVER);
            MDC.put("serverLogger", this.serverName);

            if (!irisService.checkTCPConnectionStatus(connection.getId())) {
                irisService.enableTCPConnection(serverName, connection.getId());
            }

            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        logger.info("Connect Client [{}] on port {}", serverName, serverSocket.getLocalPort());

        while (isRunning) {
            readMessage();
        }
    }

    private String readMessage() {
        try (Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            StringBuilder rawMessage = new StringBuilder();
            int current;

            while ((current = inputStream.read()) != -1) {
                char c = (char) current;

                if (c == HL7LLPCharacters.FS.getCharacter()) {
                    int next = inputStream.read();
                    if (next == HL7LLPCharacters.CR.getCharacter()) {
                        break;
                    }
                }

                rawMessage.append(c);
            }

            String fullLlpMessage = rawMessage.toString();
            String cleanTextMessage = llpToText(fullLlpMessage);

            MDC.put("serverLogger", this.serverName);
            messageLogger.addServerMessage("", cleanTextMessage);
            response(outputStream, cleanTextMessage);

            return cleanTextMessage;

        } catch (SocketTimeoutException e) {
            return null;
        } catch (Exception e) {
            logger.error("[{}] Error procesando conexión HL7", serverName, e);
        }

        return null;
    }

    protected void response(OutputStream outputStream, String receivedMessage) {
    }

    protected String extractUUID(String hl7Message) {
        String[] lines = hl7Message.split("\\r?\\n");
        for (String line : lines) {
            if (line.startsWith("MSH")) {
                String[] fields = line.split("\\|");
                if (fields.length > 10) {
                    return fields[9];
                }
            }
        }
        return null;
    }
}
