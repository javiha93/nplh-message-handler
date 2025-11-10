package org.example.server;
import com.sun.net.httpserver.HttpServer;
import org.example.client.Clients;
import org.example.client.WSClient;
import org.example.domain.host.HostType;
import org.example.domain.server.message.ServerMessage;
import org.example.domain.server.message.response.ResponseInfo;
import org.example.domain.server.message.response.ResponseStatus;
import org.example.domain.host.Connection;
import org.example.domain.ws.UPATHCLOUD.NPLHToUPATHCLOUD.AddCase.UPATHCLOUD_AddCase;
import org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder.VSS_ProcessOrder;
import org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessNewOrder.VTGWS_ProcessNewOrder;
import org.example.domain.ws.WSMessage;
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
    private HostType hostType;
    private String location;
    private final Connection connection;
    private Clients clients;

    final MessageLogger messageLogger;
    static final Logger logger = LoggerFactory.getLogger(WSServer.class);

    public WSServer(String serverName, HostType hostType, Connection connection, IrisService irisService, Clients clients) {
        this.clients = clients;
        this.location = connection.getWsLocation();
        this.hostType = hostType;
        this.connection = connection;
        this.serverName = serverName;

        ResponseStatus communicationResponse = ResponseStatus.enabled();
        ResponseStatus applicationResponse = ResponseStatus.disabled();
        if (hostType.equals(HostType.VTG)) {
            applicationResponse = ResponseStatus.enabled();
        }

        setDefaultResponse(ResponseInfo.createDefault(applicationResponse, communicationResponse));

        this.isRunning = false;

        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), irisService, this.serverName, MockType.SERVER);
        MDC.put("serverLogger", this.serverName);

        configureWSConnection(serverName, connection, irisService);
        startServer();
    }

    private void startServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(connection.getPort()), 0);

            //TODO enum hostType
            SoapHandler soapHandler = switch (hostType) {
                case VSS -> new VSSHandler(messageLogger, serverName, this);
                case VTG -> new VTGWSHandler(messageLogger, serverName,  this, (WSClient) clients.getClient(serverName));
                case DP -> new DPHandler(messageLogger, serverName, this);
                case VIRTUOSO -> new UpathCloudHandler(messageLogger, serverName, this);
                default    -> new SoapHandler(messageLogger, serverName, this);
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

    public String waitForMessage(String caseId) {
        long timeoutMillis = 10_000;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            synchronized (messages) {
                for (ServerMessage msg : messages) {
                    if (msg.getMessage() != null && msg.getMessage().contains(caseId)) {
                        return msg.getMessage();
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("[{}] Interrupted wait looking for messages with caseId {}", serverName, caseId);
                return null;
            }
        }

        logger.warn("[{}] Timeout esperando mensaje con caseId {}", serverName, caseId);
        return null;
    }

    public WSMessage waitForObjectMessage(String caseId) {
        String messageReceived = waitForMessage(caseId);

        try {
            return UPATHCLOUD_AddCase.fromXml(messageReceived);
        } catch (Exception ignored) {
        }

        try {
            return VTGWS_ProcessNewOrder.fromXml(messageReceived);
        } catch (Exception ignored) {
        }

        try {
            return VSS_ProcessOrder.fromXml(messageReceived);
        } catch (Exception ignored) {
        }
        throw new RuntimeException("");
    }

    @Override
    public void setIsRunning(Boolean isRunning) {
        boolean wasRunning = this.isRunning;
        super.setIsRunning(isRunning);

        if (isRunning && !wasRunning) {
            logger.info("Activating WS Server [{}]", serverName);
            startServer();
        } else if (!isRunning && wasRunning) {
            logger.info("Deactivating WS Server [{}]", serverName);
            stopServer();
        }
    }
}
