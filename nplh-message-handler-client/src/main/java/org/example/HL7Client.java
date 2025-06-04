package org.example;

import lombok.Getter;
import org.example.domain.host.HL7Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class HL7Client extends Client {

    static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    Socket socket;
    PrintWriter out;
    BufferedReader in;


    public HL7Client(HL7Host host) {
        this.clientName = host.name();
        try {
            socket = new Socket(host.getIp(), host.getPort());
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (UnknownHostException e) {
            logger.error("Unknown host {} , {}:{}", host.name(), host.getIp(), host.getPort(), e);
        }
        catch (IOException e) {
            logger.error("Error connecting to {}:{}", host.getIp(), host.getPort(), e);
        }
    }

    @Override
    public String send(String message) {
        String llpMessage = textToLlp(message);
        for (char c : llpMessage.toCharArray()) {
            out.write(c);
        }
        logger.info("\nSent message: \n{}\nto Host {}", message, clientName);
        out.flush();

        return receive();
    }

    private String receive() {
        try {
            StringBuilder response = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                response.append((char) c);
                if (c == HL7LLPCharacters.FS.getCharacter()) {
                    break;
                }
            }
            logger.info("\nReceived response: \n{}\nfrom Host {}",
                    llpToText(response.toString()),
                    clientName);
            return  llpToText(response.toString());
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
}
