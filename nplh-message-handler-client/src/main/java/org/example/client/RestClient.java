package org.example.client;

import org.example.client.message.ClientMessage;
import org.example.client.message.ClientMessageList;
import org.example.domain.host.RESTHost;
import org.example.domain.host.host.Connection;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import static org.example.utils.MessageHandler.formatXml;

public class RestClient extends Client {

    final org.example.logging.MessageLogger messageLogger;

    private String apikey;
    ClientMessageList clientMessageList;

    public RestClient(RESTHost host) {
        this.clientName = host.name();
        this.apikey = host.getApiKey();
        clientMessageList = new ClientMessageList();

        this.messageLogger = new org.example.logging.MessageLogger(LoggerFactory.getLogger("clients." + this.clientName), this.clientName);
        MDC.put("clientLogger", this.clientName);
    }

    public RestClient(String hostName, String hostType, Connection connection) {
        this.clientName = hostName;
        this.clientType = hostType;
        this.apikey = connection.getApiKeyValue();
        clientMessageList = new ClientMessageList();

        this.messageLogger = new org.example.logging.MessageLogger(LoggerFactory.getLogger("clients." + this.clientName), this.clientName);
        MDC.put("clientLogger", this.clientName);
    }

    @Override
    public String send(String messageType, String message, String controlId) {
        String urlString = null;
        if (Objects.equals(messageType, "RETRIEVAL")) {
            urlString = "http://127.0.0.1:7000/api/v1/automation/artifact/" + message;
        } else {
            urlString = "http://127.0.0.1:7000/api/v1/automation/artifact/" + message;
        }
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-NPLH-APIKEY", apikey);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            ClientMessage clientMessage = new ClientMessage(message);
            clientMessageList.add(clientMessage);
            MDC.put("clientLogger", this.clientName);
            messageLogger.info("[SEND]: \n\n{} \n", message);

            return receive(connection, clientMessage);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receive(HttpURLConnection connection, ClientMessage clientMessage) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            int statusCode = connection.getResponseCode();
            InputStreamReader streamReader;

            if (statusCode >= 400) {
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            response.append("ERROR: ").append(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {}
            }
        }

        clientMessage.addResponse(response.toString());
        messageLogger.info("[RECEIVE]: \n\n{} \n####################################################################\n", response);
        return response.toString();
    }

}
