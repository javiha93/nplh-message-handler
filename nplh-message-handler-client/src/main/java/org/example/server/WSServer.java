package org.example.server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.example.server.impl.SoapHandler;
import org.example.server.impl.VSSHandler;
import org.example.server.impl.VTGWSHandler;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WSServer extends Server {

    private HttpServer server;
    private String hostType;
    private String location;

    final org.example.logging.MessageLogger messageLogger;

    public WSServer(String serverName, String hostType, String location) {
        this.location = location;
        this.hostType = hostType;
        this.serverName = serverName;
        this.messageLogger = new org.example.logging.MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), this.serverName);
        MDC.put("serverLogger", this.serverName);
        try {
            String[] parts = location.split(":");
            int port = Integer.parseInt(parts[2].split("/")[0]);
            String path = "/" + parts[2].split("/", 2)[1];

            server = HttpServer.create(new InetSocketAddress(port), 0);

            SoapHandler soapHandler = switch (hostType) {
                case "VSS" -> new VSSHandler(messageLogger, serverName);
                case "VTG" -> new VTGWSHandler(messageLogger, serverName);
                default    -> new SoapHandler(messageLogger, serverName);
            };

            server.createContext(path, soapHandler);
            server.setExecutor(null);
            server.start();

            System.out.println("WS Server started at: " + location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
