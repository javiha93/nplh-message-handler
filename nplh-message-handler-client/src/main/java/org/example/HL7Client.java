package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.example.domain.host.ClientMessage;
import org.example.domain.host.HL7Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HL7Client extends Client {

    static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    ClientMessageList clientMessageList;

    public HL7Client(HL7Host host) {
        this.clientName = host.name();
        try {
            socket = new Socket(host.getIp(), host.getPort());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientMessageList = new ClientMessageList();

        } catch (UnknownHostException e) {
            logger.error("Unknown host {} , {}:{}", host.name(), host.getIp(), host.getPort(), e);
        } catch (IOException e) {
            logger.error("Error connecting to {}:{}", host.getIp(), host.getPort(), e);
        }
    }

    @Override
    public List<String> send(String message, String controlId) {
        String llpMessage = textToLlp(message);
        ClientMessage clientMessage = new ClientMessage(message, controlId);
        clientMessageList.add(clientMessage);
        for (char c : llpMessage.toCharArray()) {
            out.write(c);
        }
        logger.info("\nSent message: \n{}\nto Host {}", message, clientName);
        out.flush();

        return receive(controlId);
    }

    private List<String> receive(String controlId) {
        List<String> responses = new ArrayList<>();
        final int initialTimeout = 4000;
        final int extendedTimeout = 300;
        try {
            socket.setSoTimeout(initialTimeout);

            String firstResponse = receiveSingle(controlId);
            if (firstResponse != null && !firstResponse.trim().isEmpty()) {
                responses.add(firstResponse);
                logger.info("Received first response");

                socket.setSoTimeout(extendedTimeout);
                String extraResponse = receiveSingle(controlId);
                if (extraResponse != null && !extraResponse.trim().isEmpty()) {
                    responses.add(extraResponse);
                    logger.info("Received second response");
                }
            }
        } catch (IOException e) {
            logger.error("Socket error", e);
        }

        return responses;
    }

    private String receiveSingle(String controlId) {
        try {
            StringBuilder response = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                response.append((char) c);
                if (c == HL7LLPCharacters.FS.getCharacter()) {
                    break;
                }
            }
            String responseText = llpToText(response.toString());
            if (!responseText.contains(controlId)) {
                logger.info("\nReceived response for another message, expected: \n{}\nfrom Host {}", responseText, clientName);
                List<String> uuids = extractUUIDs(responseText);
                for (String uuid : uuids) {
                    ClientMessage clientMessage = clientMessageList.getMessageByControlId(uuid);
                    if (clientMessage != null) {
                        clientMessage.addResponse(responseText);
                        //ONLY UI CODE NOT FOR AT SOLUTION
                        notifyUIMessageUpdate(uuid, clientMessage.getResponses());
                        break;
                    }
                }

                socket.setSoTimeout(4000);
                return receiveSingle(controlId);
            }
            ClientMessage message = clientMessageList.getMessageByControlId(controlId);
            if (message != null) {
                message.addResponse(responseText);
            }
            logger.info("\nReceived response: \n{}\nfrom Host {}", responseText, clientName);
            return responseText;
        } catch (SocketTimeoutException e) {
            logger.info("Not found any response");
            return null;
        } catch (IOException e) {
            logger.error("Error reading response from host {}", clientName, e);
            return null;
        }
    }

    private String textToLlp(String textMessage) {
        return HL7LLPCharacters.VT.getCharacter()
                + textMessage.trim().replaceAll("\\n", HL7LLPCharacters.CR.getCharacterAsString())
                + HL7LLPCharacters.CR.getCharacter()
                + HL7LLPCharacters.FS.getCharacter()
                + HL7LLPCharacters.CR.getCharacter();
    }

    private String llpToText(String llpMessage) {
        if (llpMessage == null || llpMessage.isEmpty()) {
            return llpMessage;
        }

        String trimmedMessage = llpMessage.startsWith(HL7LLPCharacters.VT.getCharacterAsString())
                ? llpMessage.substring(1)
                : llpMessage;

        int fsIndex = trimmedMessage.indexOf(HL7LLPCharacters.FS.getCharacter());
        if (fsIndex >= 0) {
            trimmedMessage = trimmedMessage.substring(0, fsIndex);
        }

        return trimmedMessage.replaceAll(HL7LLPCharacters.CR.getCharacterAsString(), "\n").trim();
    }

    @Getter
    public enum HL7LLPCharacters {

        VT(0x0b), FS(0x1c), CR(0x0d);

        final char character;

        HL7LLPCharacters(int character) {
            this.character = (char) character;
        }

        public String getCharacterAsString() {
            return String.valueOf(character);
        }

    }

    public List<String> extractUUIDs(String message) {
        List<String> uuids = new ArrayList<>();
        Pattern uuidPattern = Pattern.compile(
                "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = uuidPattern.matcher(message);
        while (matcher.find()) {
            uuids.add(matcher.group());
        }

        return uuids;
    }

    //ONLY UI CODE NOT FOR AT SOLUTION
    public void notifyUIMessageUpdate(String controlId, List<String> responses) {
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

    public void notifyUIMessageUpdateSync(String controlId, List<String> responses) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("controlId", controlId);
            payload.put("responses", responses);

            ObjectMapper mapper = new ObjectMapper();
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
