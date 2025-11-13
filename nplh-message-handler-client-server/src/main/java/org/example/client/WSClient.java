package org.example.client;

import org.example.domain.client.message.ClientMessage;
import org.example.domain.client.message.ClientMessageList;
import org.example.domain.host.HostType;
import org.example.utils.MessageLogger;
import org.example.domain.host.Connection;
import org.example.service.IrisService;
import org.example.utils.MockType;
import org.example.utils.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

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
    private final String requestorAddress;
    private Map<String, String> headers = new HashMap<>();
    ClientMessageList clientMessageList;

    public WSClient(String hostName, HostType hostType, Connection connection, IrisService irisService) {
        this.clientName = hostName;
        this.clientType = hostType;
        this.irisService = irisService;
        this.requestorAddress = connection.getRequestorAddress();

        irisService.enableWSConnection(hostName, connection.getId());
        this.baseUrl = "http://127.0.0.1:" + 80 + connection.getWsName();
        if (!connection.getApiKeyValue().isEmpty()) {
            this.headers = Map.of(connection.getApiKeyTag(), connection.getApiKeyValue());
        }
        clientMessageList = new ClientMessageList();

        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("clients." + this.clientName), irisService, this.clientName, MockType.CLIENT);
        MDC.put("clientLogger", this.clientName);
        logger.info("Connect Client {} on url {}", clientName, this.baseUrl);

        if (requestorAddress != null && !requestorAddress.isEmpty()) {
            ProxyHelper.configureIP(requestorAddress);
        }
    }

    public String send(String soapAction, String messageBody) {
        return send(soapAction, messageBody, "");
    }

    public String send(String soapAction, String messageBody, String controlId) {
        String requestBody = buildSoapEnvelope(messageBody, this.clientName);

        try {
            String targetUrl = baseUrl + ".CLS";
            String response;

            response = ProxyHelper.sendWSMessage(targetUrl, requestorAddress, soapAction, headers, requestBody);

            logger.info("\nSent message: \n{}\nto Host {}", messageBody, clientName);
            ClientMessage clientMessage = new ClientMessage(messageBody);
            clientMessageList.add(clientMessage);
            MDC.put("clientLogger", this.clientName);

            String soapResponse = extractSoapResponse(response);
            logger.info("\nReceived response: \n{}\nfrom Host {}", soapResponse, clientName);
            clientMessage.addResponse(soapResponse);
            messageLogger.addClientMessage("", controlId, messageBody, List.of(formatXml(soapResponse)));

            return soapResponse;

        } catch (Exception e) {
            logger.error("Error sending message: {} to {}, Error:", messageBody, clientName, e);
            return "";
        }
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
