package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.domain.server.message.ServerMessage;
import org.example.server.WSServer;
import org.example.service.UINotificationService;
import org.example.utils.MessageLogger;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SoapHandler implements HttpHandler {

    private final MessageLogger messageLogger;
    private final String serverName;
    protected WSServer server;
    protected String messageReceived;
    protected Boolean isSuccessful;

    public SoapHandler(MessageLogger messageLogger, String serverName, WSServer server) {
        this.messageLogger = messageLogger;
        this.serverName = serverName;
        this.server = server;
        this.isSuccessful = true;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MDC.put("serverLogger", this.serverName);
        messageReceived = new String(exchange.getRequestBody().readAllBytes());

        //messageLogger.info("[RECEIVE]: \n\n{} \n", messageReceived);
        String soapAction = getSoapAction(exchange);
        List<String> responses = response(exchange, soapAction);

        messageLogger.addServerMessage(getCaseId(messageReceived), messageReceived, responses);

        ServerMessage serverMessage = new ServerMessage(messageReceived, responses);
        server.getMessages().add(serverMessage);
        UINotificationService.addServerMessage(serverName, serverMessage);
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

    protected String buildResponse(HttpExchange exchange, String soapAction) {
        return null;
    }

    protected String getCaseId(String message) {
        return null;
    }


}
