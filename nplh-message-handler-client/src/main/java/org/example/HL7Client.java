package org.example;

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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HL7Client extends Client {

    static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public List<ClientMessage> messageList;

    public HL7Client(HL7Host host) {
        this.clientName = host.name();
        try {
            socket = new Socket(host.getIp(), host.getPort());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            messageList = new ArrayList<>();

        } catch (UnknownHostException e) {
            logger.error("Unknown host {} , {}:{}", host.name(), host.getIp(), host.getPort(), e);
        } catch (IOException e) {
            logger.error("Error connecting to {}:{}", host.getIp(), host.getPort(), e);
        }
    }

    @Override
    public List<String> send(String message, String controlId) {
        String llpMessage = textToLlp(message);
        ClientMessage clientMessage = new ClientMessage(message);
        messageList.add(clientMessage);
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
                for (String uuid: uuids) {
                    ClientMessage clientMessage = getMessageByControlId(uuid);
                    if (clientMessage != null) {
                        clientMessage.addResponse(responseText);
                        break;
                    }
                }

                socket.setSoTimeout(4000);
                return receiveSingle(controlId);
            }
            ClientMessage message = getMessageByControlId(controlId);
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

    private ClientMessage getMessageByControlId(String messageControlID) {
        for (ClientMessage message: messageList) {
            if (message.getMessage().contains(messageControlID)) {
                return message;
            }
        }
        logger.error("Not found any message with the following control Id {}", messageControlID);
        return null;
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
}
