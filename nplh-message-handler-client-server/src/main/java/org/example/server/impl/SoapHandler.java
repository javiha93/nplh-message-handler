package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.client.WSClient;
import org.example.domain.host.HostType;
import org.example.domain.server.message.ServerMessage;
import org.example.server.WSServer;
import org.example.service.UINotificationService;
import org.example.utils.MessageLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SoapHandler implements HttpHandler {

    private final MessageLogger messageLogger;
    private final String serverName;
    protected WSServer server;
    protected String messageReceived;
    protected Boolean isSuccessful;

    static final Logger logger = LoggerFactory.getLogger(SoapHandler.class);

    public SoapHandler(MessageLogger messageLogger, String serverName, WSServer server) {
        this.messageLogger = messageLogger;
        this.serverName = serverName;
        this.server = server;
        this.isSuccessful = true;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MDC.put("serverLogger", this.serverName);

        logger.info("Receiving message from WS {}", serverName);
        final String requestBody = new String(exchange.getRequestBody().readAllBytes());

        logger.info("Received message from {}: {}", serverName, requestBody);
        //messageLogger.info("[RECEIVE]: \n\n{} \n", messageReceived);
        String soapAction = getSoapAction(exchange);

        List<String> responses;
        if (this.server.getHostType().equals(HostType.VTG)) {
            responses = response(exchange, soapAction, requestBody);
        } else {
            responses = response(exchange, soapAction);
        }

        final List<String> finalResponses = responses;
        final String finalCaseId = getCaseId(requestBody);

        server.getThreadPool().execute(() -> {
            MDC.put("serverLogger", serverName);

            try {
                synchronized (server.getMessages()) {

                    messageLogger.addServerMessage(finalCaseId, requestBody, finalResponses);
                    ServerMessage serverMessage = new ServerMessage(requestBody, finalResponses);
                    server.getMessages().add(serverMessage);

                    UINotificationService.addServerMessage(serverName, serverMessage);
                }
            } catch (Exception e) {
                logger.error("Error executing ASYNC post-response tasks for {}", serverName, e);
            } finally {
                MDC.remove("serverLogger");
            }
        });
    }

    //TODO Refactor
    private String getSoapAction(HttpExchange exchange) {
        String fullSoapAction = exchange.getRequestHeaders().getFirst("Soapaction");
        if (fullSoapAction == null || fullSoapAction.isEmpty()) {
            return "";
        }
        
        // Remover comillas si están presentes
        fullSoapAction = fullSoapAction.replace("\"", "");
        
        // Manejar diferentes patrones de URL
        // Patrón 1: http://ventanamed.com/vantage/ventanaconnect/IVCtoVTG_Service/ProcessNewOrder
        // Patrón 2: http://webservice.dp600.ventana.com//ws.ext.DP600.VCToDP600WebService.addSlide
        
        if (fullSoapAction.contains("/")) {
            // Obtener la última parte después del último "/"
            String[] parts = fullSoapAction.split("/");
            String lastPart = parts[parts.length - 1];
            
            // Si la última parte contiene un punto, tomar la parte después del último punto
            if (lastPart.contains(".")) {
                String[] dotParts = lastPart.split("\\.");
                return dotParts[dotParts.length - 1];
            }
            
            return lastPart;
        }
        
        return fullSoapAction;
    }

    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        String soapResponse = "";

        exchange.getResponseHeaders().set("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, soapResponse.length());
        exchange.getResponseBody().write(soapResponse.getBytes());
        exchange.getResponseBody().close();

        return Collections.singletonList(soapResponse);
    }

    protected List<String> response(HttpExchange exchange, String soapAction, String requestBody) throws IOException {
        String soapResponse = "";

        exchange.getResponseHeaders().set("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, soapResponse.length());
        exchange.getResponseBody().write(soapResponse.getBytes());
        exchange.getResponseBody().close();

        return Collections.singletonList(soapResponse);
    }

    protected String buildResponse(HttpExchange exchange, String soapAction) {
        return null;
    }

    protected String getCaseId(String message) {
        return null;
    }


}
