package org.example.server;

import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.LIS_SCAN_SLIDE;
import org.example.domain.hl7.VTG.NPLHToVTG.VTG_OML21;
import org.example.domain.host.Connection;
import org.example.domain.host.HostType;
import org.example.domain.server.message.ServerMessage;
import org.example.service.IrisService;
import org.example.service.UINotificationService;
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
import java.util.List;

import static org.example.utils.MessageHandler.llpToText;

public class HL7Server extends Server implements Runnable {
    static final Logger logger = LoggerFactory.getLogger(HL7Server.class);
    private final MessageLogger messageLogger;

    IrisService irisService;
    ServerSocket serverSocket;
    private Thread serverThread;

    private final int port;
    private final HostType hostType;

    public HL7Server(String hostName, HostType hostType, Connection connection, IrisService irisService) {
        serverName = hostName;

        this.hostType = hostType;
        this.port = connection.getPort();
        this.isRunning = true;
        this.irisService = irisService;
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), irisService, this.serverName, MockType.SERVER);
        MDC.put("serverLogger", this.serverName);

        if (!irisService.checkTCPConnectionStatus(connection.getId())) {
            irisService.enableTCPConnection(serverName, connection.getId());
        }

        startServerThread();
    }

    private void startServerThread() {
        if (serverThread == null || !serverThread.isAlive()) {
            try {
                if (serverSocket == null || serverSocket.isClosed()) {
                    serverSocket = new ServerSocket(port);
                    serverSocket.setSoTimeout(1000);
                    logger.info("Recreated server socket for [{}] on port {}", serverName, port);
                }
            } catch (IOException e) {
                logger.error("Error recreating server socket for [{}]: {}", serverName, e.getMessage());
                return;
            }

            serverThread = new Thread(this);
            serverThread.setDaemon(true);
            serverThread.start();
        }
    }

    public void setIsRunning(Boolean isRunning) {
        super.setIsRunning(isRunning);

        if (isRunning) {
            startServerThread();
        } else {
            logger.info("Stopping server [{}] on port {}", serverName, serverSocket.getLocalPort());

            if (serverThread != null && serverThread.isAlive()) {
                serverThread.interrupt();
            }

            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                logger.warn("Error closing server socket for [{}]: {}", serverName, e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        logger.info("Connect Client [{}] on port {}", serverName, serverSocket.getLocalPort());

        while (isRunning && !Thread.currentThread().isInterrupted()) {
            readMessage();
        }
    }

    private void readMessage() {
        try (Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            if (!isRunning) {
                return;
            }

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

            if (!isRunning) {
                return;
            }

            String fullLlpMessage = rawMessage.toString();
            String cleanTextMessage = llpToText(fullLlpMessage);

            //LoggerMessage
            MDC.put("serverLogger", this.serverName);

            try {
                List<String> responses = response(outputStream, cleanTextMessage);
                ServerMessage serverMessage = new ServerMessage(cleanTextMessage, responses);

                messages.add(serverMessage);
                messageLogger.addServerMessage("", cleanTextMessage, responses);
                //UI DELETE ON AT
                UINotificationService.addServerMessage(serverName, serverMessage);
            } catch (Exception e) {
                logger.info("[{}] Unable to create and send a response for message: {}", serverName, cleanTextMessage);
                ServerMessage serverMessage = new ServerMessage(cleanTextMessage);

                messages.add(serverMessage);
                messageLogger.addServerMessage("", cleanTextMessage);
                //UI DELETE ON AT
                UINotificationService.addServerMessage(serverName, serverMessage);
            }

        } catch (SocketTimeoutException ignored) {
        } catch (Exception e) {
            logger.error("[{}] Error processing HL7 connection", serverName, e);
        }
    }

    public String waitForMessage(String caseId) {
        long timeoutMillis = 10_000;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            synchronized (messages) {
                for (ServerMessage msg : messages) {
                    if (msg.getMessage() != null && msg.getMessage().contains(caseId)) {
                        return msg.getMessage();
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("[{}] Interrupted wait looking for messages with caseId {}", serverName, caseId);
                return null;
            }
        }

        logger.warn("[{}] Timeout esperando mensaje con caseId {}", serverName, caseId);
        return null;
    }

    public HL7Message waitForObjectMessage(String caseId) {
        String messageReceived = waitForMessage(caseId);

        if (this.hostType.equals(HostType.VTG)) {
            try {
                return VTG_OML21.fromString(messageReceived);
            } catch (Exception ignored) {
            }
        } else if (this.hostType.equals(HostType.LIS)) {
            try {
                return LIS_SCAN_SLIDE.fromString(messageReceived);
            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException("");
    }

    // TO DO: check abstract
    protected List<String> response(OutputStream outputStream, String receivedMessage) {
        return null;
    }

    protected String formatHL7Response(String ack) {
        return HL7LLPCharacters.VT.getCharacter() +
                ack.replace('\n', HL7LLPCharacters.CR.getCharacter()) +
                HL7LLPCharacters.FS.getCharacter() +
                HL7LLPCharacters.CR.getCharacter();
    }

    protected void sendResponse(OutputStream outputStream, String response) {
        try {
            outputStream.write(response.getBytes());
            outputStream.flush();

            logger.info("Sent response: {}", response);

        } catch (IOException e) {
            logger.error("Error sending response", e);
        }
    }

    //Refactor for message parser
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
