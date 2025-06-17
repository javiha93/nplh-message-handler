package org.example.server;

import org.example.domain.host.HL7Host;
import org.example.utils.HL7LLPCharacters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.example.utils.MessageHandler.llpToText;

public class HL7Server extends Server implements Runnable {
    static final Logger logger = LoggerFactory.getLogger(HL7Server.class);
    ServerSocket serverSocket;
    boolean isRunning;

    public HL7Server(HL7Host host) {
        try {
            serverSocket = new ServerSocket(host.getServerPort());
            this.isRunning = true;

            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        logger.info("[{}] Servidor HL7 escuchando en puerto {}", serverName, serverSocket.getLocalPort());

        while (isRunning) {
            readMessage();
        }
    }

    private String readMessage() {
        try (Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream()) {

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

            logger.info("[{}] Mensaje interceptado:\n{}", serverName, cleanTextMessage);

            return cleanTextMessage;

        } catch (Exception e) {
            logger.error("[{}] Error procesando conexión HL7", serverName, e);
        }
        return null;
    }

}
