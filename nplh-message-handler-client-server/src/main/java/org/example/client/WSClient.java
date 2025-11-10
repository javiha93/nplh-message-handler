package org.example.client;

import org.example.domain.client.message.ClientMessage;
import org.example.domain.client.message.ClientMessageList;
import org.example.domain.host.HostType;
import org.example.utils.MessageLogger;
import org.example.domain.host.Connection;
import org.example.service.IrisService;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.utils.MessageHandler.formatXml;
import static org.example.utils.SoapMessageHandler.buildSoapEnvelope;

public class WSClient extends Client {

    static final Logger logger = LoggerFactory.getLogger(WSClient.class);
    final MessageLogger messageLogger;
    IrisService irisService;

    private final String baseUrl;
    private Map<String, String> headers = new HashMap<>();
    ClientMessageList clientMessageList;

    public WSClient(String hostName, HostType hostType, Connection connection, IrisService irisService) {
        this.clientName = hostName;
        this.clientType = hostType;
        this.irisService = irisService;

        irisService.enableWSConnection(hostName, connection.getId());
        this.baseUrl = "http://127.0.0.1:" + 80 + connection.getWsName();
        if (!connection.getApiKeyValue().isEmpty()) {
            this.headers = Map.of(connection.getApiKeyTag(), connection.getApiKeyValue());
        }
        clientMessageList = new ClientMessageList();

        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("clients." + this.clientName), irisService, this.clientName, MockType.CLIENT);
        MDC.put("clientLogger", this.clientName);
        logger.info("Connect Client {} on url {}", clientName, this.baseUrl);
    }

    public String send(String soapAction, String messageBody) {
        return send(soapAction, messageBody, "");
    }

    public String send(String soapAction, String messageBody, String controlId) {
        String requestBody = buildSoapEnvelope(messageBody, this.clientName);

        try {
            URL oURL = new URL(baseUrl + ".CLS");
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", soapAction);

            if (headers != null) {
                for (var header : headers.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            try {
                OutputStream reqStream = con.getOutputStream();
                reqStream.write(requestBody.getBytes());
                logger.info("\nSent message: \n{}\nto Host {}", messageBody, clientName);
                ClientMessage clientMessage = new ClientMessage(messageBody);
                clientMessageList.add(clientMessage);
                MDC.put("clientLogger", this.clientName);


                StringBuilder r = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = br.readLine()) != null) {
                    r.append(line);
                }
                br.close();

                String soapResponse = extractSoapResponse(r.toString());
                logger.info("\nReceived response: \n{}\nfrom Host {}", soapResponse, clientName);
                clientMessage.addResponse(soapResponse);
                messageLogger.addClientMessage("", controlId, messageBody, List.of(formatXml(soapResponse)));

                return extractSoapResponse(r.toString());
            } catch (Exception e) {
                String errorResponse = "";

                if (e instanceof IOException && con != null) {
                    try (BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                        String line;
                        StringBuilder errorBuilder = new StringBuilder();
                        while ((line = errorReader.readLine()) != null) {
                            errorBuilder.append(line);
                        }
                        errorResponse = errorBuilder.toString();
                        logger.error("Host {} responded with error:\n{}", clientName, errorResponse);
                    } catch (Exception innerEx) {
                        logger.error("Failed to read error response from host {}", clientName, innerEx);
                    }
                }

                logger.error("Error sending message: {} to {}, Error:", messageBody, clientName, e);
                return errorResponse;
            }
        } catch (Exception e) {

            logger.error("Error sending message: {} to {}, Error:", messageBody, clientName, e);
        }
        return null;
    }

    private String extractSoapResponse(String response) {
        Pattern pattern = Pattern.compile("<SOAP-ENV:Body>(.*?)</SOAP-ENV:Body>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            response = matcher.group(1);
        }
        return response.replaceAll("types:", "")
                .replaceAll(" xsi:type\\s*=\\s*\"[^\"]*\"", "");
    }
}
