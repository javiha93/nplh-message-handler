package org.example.server;

import org.example.client.HL7Client;
import org.example.domain.host.HL7Host;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HL7Server extends Server implements Runnable{
    static final Logger logger = LoggerFactory.getLogger(HL7Server.class);
    ServerSocket serverSocket;
    Socket socket;
    BufferedReader in;
    public HL7Server(HL7Host host) {
        try {
            serverSocket = new ServerSocket(host.getServerPort());

            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            socket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            StringBuilder message = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null && !line.isEmpty()) {
                message.append(line).append("\n");
            }

            logger.info("[{}] Mensaje recibido:\n{}", serverName, message);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
