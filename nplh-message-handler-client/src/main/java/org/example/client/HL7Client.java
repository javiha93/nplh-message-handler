package org.example.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.domain.host.host.Connection;
import org.example.utils.MessageLogger;
import org.example.service.IrisService;
import org.example.utils.HL7LLPCharacters;
import org.example.client.message.ClientMessage;
import org.example.client.message.ClientMessageList;
import org.example.client.message.ClientMessageResponse;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.example.utils.MessageHandler.*;

public class HL7Client extends Client {

    static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    final MessageLogger messageLogger;
    IrisService irisService;

    Socket socket;
    PrintWriter out;
    BufferedReader in;
    Connection connection;
    ClientMessageList clientMessageList;

    public HL7Client(String hostName, String hostType, Connection connection, IrisService irisService) {
        this.clientName = hostName;
        this.clientType = hostType;
        this.connection = connection;
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("clients." + this.clientName), irisService, this.clientName, MockType.CLIENT);
        this.irisService = irisService;

        MDC.put("clientLogger", this.clientName);
        clientMessageList = new ClientMessageList();

        openSocket();
    }

    @Override
    public void send(String message, String controlId) {
        openSocket();

        String llpMessage = textToLlp(message);
        ClientMessage clientMessage = new ClientMessage(message, controlId);
        clientMessageList.add(clientMessage);
        MDC.put("clientLogger", this.clientName);
        messageLogger.addClientMessage("TEST", controlId, message);

        //messageLogger.info("[SEND][{}]: \n\n{} \n\n####################################################################\n", controlId, message);
        for (char c : llpMessage.toCharArray()) {
            out.write(c);
        }
        logger.info("\nSent message: \n{}\nto Host {}", message, clientName);
        out.flush();
    }

    private void startAsyncReceiver() {
        CompletableFuture.runAsync(() -> {
            while (!socket.isClosed()) {
                try {
                    StringBuilder response = new StringBuilder();
                    int c;
                    while ((c = in.read()) != -1) {
                        response.append((char) c);
                        if (c == HL7LLPCharacters.FS.getCharacter()) break;
                    }

                    if (response.isEmpty()) {
                        closeSocket();
                        return;
                    }

                    String responseText = llpToText(response.toString());

                    List<String> uuids = extractUUIDs(responseText);
                    if (!uuids.isEmpty()) {
                        for (String uuid : uuids) {
                            ClientMessage clientMessage = clientMessageList.getMessageByControlId(uuid);
                            if (clientMessage != null) {
                                clientMessage.addResponse(responseText);
                                MDC.put("clientLogger", this.clientName);
                                messageLogger.addResponse(uuid, responseText);
                                //messageLogger.injectReceive(uuid, responseText);
                                notifyUIMessageUpdate(uuid, clientMessage.getResponses());
                                logger.info("Asynchronously received: \n{}\nand matched response for {}", responseText, uuid);
                                break;
                            }
                        }
                    } else {
                        logger.warn("Received unmatched response: {}", responseText);
                    }

                } catch (SocketTimeoutException e) {
                    logger.debug("Timeout exception fo host {}", clientName);
                } catch (IOException e) {
                    closeSocket();
                    logger.error("Error in async receive loop for host {}", clientName, e.getMessage());
                    break;
                }
            }
        });
    }

    public void closeSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                logger.info("Closed {} socket client", clientName);
            }
        } catch (IOException e) {
            logger.error("Unable to close {} socket client", clientName);
        }
    }

    public void openSocket() {
        try {
            if (socket == null || socket.isClosed()) {
                if (!irisService.checkTCPConnectionStatus(connection.getId())) {
                    irisService.enableTCPConnection(clientName, connection.getId());
                }

                socket = new Socket(this.connection.getIp().isEmpty() ? "127.0.0.1" : this.connection.getIp(), this.connection.getPort());
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                logger.info("Connect Client {} on port {}", clientName, this.connection.getPort());

                startAsyncReceiver();
            }

        } catch (UnknownHostException e) {
            logger.error("Unknown host {} , {}:{}", clientName, connection.getIp().isEmpty() ? "127.0.0.1" : connection.getIp(), connection.getPort(), e);
        } catch (IOException e) {
            logger.error("Error connecting to {}:{}", connection.getIp().isEmpty() ? "127.0.0.1" : connection.getIp(), connection.getPort(), e);
        }
    }

    //ONLY UI CODE NOT FOR AT SOLUTION
    public void notifyUIMessageUpdate(String controlId, List<ClientMessageResponse> responses) {
        // Execute notification asynchronously with delay to avoid blocking the main request
        CompletableFuture.runAsync(() -> {
            try {
                // Wait a bit to ensure the main HTTP request has been fully processed
                Thread.sleep(500);
                notifyUIMessageUpdateSync(controlId, responses);
            } catch (InterruptedException e) {
                logger.warn("UI notification interrupted for controlId: {}", controlId);
                Thread.currentThread().interrupt();
            }
        });
    }

    public void notifyUIMessageUpdateSync(String controlId, List<ClientMessageResponse> responses) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("controlId", controlId);
            payload.put("responses", responses);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String jsonPayload = mapper.writeValueAsString(payload);

            // Opción 1: Usar Base64 para evitar problemas de escape
            String base64Json = Base64.getEncoder().encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));

            String powerShellCommand = String.format(
                    "[Console]::OutputEncoding = [System.Text.Encoding]::UTF8; " +
                            "$jsonBytes = [Convert]::FromBase64String('%s'); " +
                            "$body = [System.Text.Encoding]::UTF8.GetString($jsonBytes); " +
                            "Invoke-RestMethod -Uri 'http://localhost:8084/api/ui/messages/update-responses' " +
                            "-Method POST -ContentType 'application/json' -Body $body",
                    base64Json
            );

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "powershell.exe",
                    "-Command",
                    powerShellCommand
            );

            Process process = processBuilder.start();
            String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
            String errors = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines().collect(Collectors.joining("\n"));

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info (powerShellCommand);
                logger.info("PowerShell ejecutado correctamente. Salida:\n{}", output);
            } else {
                logger.error("Error en PowerShell (Código {}):\n{}", exitCode, errors);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al ejecutar PowerShell: ", e);
            Thread.currentThread().interrupt();
        }
    }
}
