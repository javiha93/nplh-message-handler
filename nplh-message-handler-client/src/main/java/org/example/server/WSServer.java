package org.example.server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WSServer extends Server {

    private HttpServer server;
    private String location;

    final org.example.logging.MessageLogger messageLogger;

    public WSServer(String serverName, String location) {
        this.location = location;
        this.serverName = serverName;
        this.messageLogger = new org.example.logging.MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), this.serverName);
        MDC.put("serverLogger", this.serverName);
        try {
            String[] parts = location.split(":");
            int port = Integer.parseInt(parts[2].split("/")[0]);
            String path = "/" + parts[2].split("/", 2)[1];

            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(path, new SoapHandler(messageLogger, serverName));
            server.setExecutor(null);
            server.start();

            System.out.println("WS Server started at: " + location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SoapHandler implements HttpHandler {

        private final org.example.logging.MessageLogger messageLogger;
        private final String serverName;

        public SoapHandler(org.example.logging.MessageLogger messageLogger, String serverName) {
            this.messageLogger = messageLogger;
            this.serverName = serverName;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            MDC.put("serverLogger", this.serverName);
            String requestBody = new String(exchange.getRequestBody().readAllBytes());
            messageLogger.info("[RECEIVE]: \n\n{} \n", requestBody);
//            System.out.println("Received SOAP message: " + requestBody);
//
//            // Crear respuesta SOAP
//            String soapResponse = buildSoapResponse(requestBody);
//
//            // Enviar respuesta
//            exchange.getResponseHeaders().set("Content-Type", "text/xml");
//            exchange.sendResponseHeaders(200, soapResponse.length());
//            exchange.getResponseBody().write(soapResponse.getBytes());
//            exchange.getResponseBody().close();
        }

    }
}
