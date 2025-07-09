package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class SoapHandler implements HttpHandler {

    private final org.example.logging.MessageLogger messageLogger;
    private final String serverName;
    protected Boolean isSuccessful;

    public SoapHandler(org.example.logging.MessageLogger messageLogger, String serverName) {
        this.messageLogger = messageLogger;
        this.serverName = serverName;
        this.isSuccessful = true;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MDC.put("serverLogger", this.serverName);
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        messageLogger.info("[RECEIVE]: \n\n{} \n", requestBody);
        String soapAction = getSoapAction(exchange);
        response(exchange, soapAction);
    }

    private String getSoapAction(HttpExchange exchange) {
        String fullSoapAction = exchange.getRequestHeaders().getFirst("Soapaction");
        return Paths.get(URI.create(fullSoapAction).getPath()).getFileName().toString();
    }

    protected void response(HttpExchange exchange, String soapAction) throws IOException {
    }


}
