package org.example.client;

import org.example.domain.host.RESTHost;
import org.example.domain.host.host.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class RestClient extends Client {

    private String apikey;
    public RestClient(RESTHost host) {
        this.clientName = host.name();
        this.apikey = host.getApiKey();
    }

    public RestClient(String hostName, Connection connection) {
        this.clientName = hostName;
        this.apikey = connection.getApiKeyValue();
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

            return receive(connection);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receive(HttpURLConnection connection) {
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

        return response.toString();
    }

}
