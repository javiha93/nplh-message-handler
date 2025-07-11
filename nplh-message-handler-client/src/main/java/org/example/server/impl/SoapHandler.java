package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class SoapHandler implements HttpHandler {

    private final org.example.logging.MessageLogger messageLogger;
    private final String serverName;
    protected String messageReceived;
    protected Boolean isSuccessful;

    public SoapHandler(org.example.logging.MessageLogger messageLogger, String serverName) {
        this.messageLogger = messageLogger;
        this.serverName = serverName;
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
    }

    private String getSoapAction(HttpExchange exchange) {
        String fullSoapAction = exchange.getRequestHeaders().getFirst("Soapaction");
        return Paths.get(URI.create(fullSoapAction).getPath()).getFileName().toString();
    }

    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        String soapResponse = buildResponse(exchange, soapAction);

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
