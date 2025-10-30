package org.example.server;
import com.sun.net.httpserver.HttpServer;
import org.example.domain.ResponseStatus;
import org.example.domain.host.host.Connection;
import org.example.server.impl.*;
import org.example.service.IrisService;
import org.example.utils.MessageLogger;
import org.example.utils.MockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetSocketAddress;

public class WSServer extends Server {

    private HttpServer server;
    private String hostType;
    private String location;
    private final Connection connection;

    final MessageLogger messageLogger;
    static final Logger logger = LoggerFactory.getLogger(WSServer.class);

    public WSServer(String serverName, String hostType, Connection connection, IrisService irisService) {
        this.location = connection.getWsLocation();
        this.hostType = hostType;
        this.connection = connection;
        this.serverName = serverName;
        this.communicationResponse = ResponseStatus.enabled();
        this.isRunning = false;

        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), irisService, this.serverName, MockType.SERVER);
        MDC.put("serverLogger", this.serverName);

        configureWSConnection(serverName, connection, irisService);
        startServer();
    }

    private void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(connection.getPort()), 0);

            SoapHandler soapHandler = switch (hostType) {
                case "VSS" -> new VSSHandler(messageLogger, serverName);
                case "VTG" -> new VTGWSHandler(messageLogger, serverName);
                case "DP" -> new DPHandler(messageLogger, serverName);
                case "VIRTUOSO" -> new UpathCloudHandler(messageLogger, serverName);
                default    -> new SoapHandler(messageLogger, serverName);
            };

            server.createContext(connection.getPath(), soapHandler);
            server.setExecutor(null);
            server.start();
            isRunning = true;

            logger.info("Connect Server [{}] at location {}", serverName, location);
        } catch (Exception e) {
            logger.error("Error starting WS Server [{}]: {}", serverName, e.getMessage(), e);
            isRunning = false;
        }
    }

    private void stopServer() {
        if (server != null) {
            try {
                logger.info("Stopping WS Server [{}]", serverName);
                server.stop(0);
                server = null;
                logger.info("WS Server [{}] stopped successfully", serverName);
            } catch (Exception e) {
                logger.error("Error stopping WS Server [{}]: {}", serverName, e.getMessage(), e);
            }
        }
    }

    private void configureWSConnection(String serverName, Connection connection, IrisService irisService) {
        if (connection.getWsLocation() != null) {
            if ((connection.getApiKeyValue() != null) && (connection.getApiKeyFile() != null)) {
                irisService.configWSConnection(connection.getId(), connection.getWsLocation(), connection.getApiKeyFile(), connection.getApiKeyValue());
            } else {
                irisService.configWSConnection(connection.getId(), connection.getWsLocation());
            }
        }

        if (!irisService.checkWSConnectionStatus(connection.getId())) {
            irisService.enableWSConnection(serverName, connection.getId());
        }
    }

    @Override
    public void setIsRunning(Boolean isRunning) {
        boolean wasRunning = this.isRunning;
        super.setIsRunning(isRunning);

        if (isRunning && !wasRunning) {
            // Activar servidor: crear y iniciar HttpServer
            logger.info("Activating WS Server [{}]", serverName);
            startServer();
        } else if (!isRunning && wasRunning) {
            // Desactivar servidor: parar HttpServer
            logger.info("Deactivating WS Server [{}]", serverName);
            stopServer();
        }
    }

    // Getters públicos para Jackson serialization
    public String getHostType() {
        return hostType;
    }

    public String getLocation() {
        return location;
    }

    public void setHostType(String hostType) {
        this.hostType = hostType;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
