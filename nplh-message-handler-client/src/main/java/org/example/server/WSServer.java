package org.example.server;
import com.sun.net.httpserver.HttpServer;
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
    private IrisService irisService;

    final MessageLogger messageLogger;
    static final Logger logger = LoggerFactory.getLogger(WSServer.class);

    public WSServer(String serverName, String hostType, Connection connection, IrisService irisService) {
        this.location = connection.getWsLocation();
        this.hostType = hostType;
        this.serverName = serverName;
        this.communicationResponse = true;

        this.irisService = irisService;
        this.messageLogger = new MessageLogger(LoggerFactory.getLogger("servers." + this.serverName), irisService, this.serverName, MockType.SERVER);
        MDC.put("serverLogger", this.serverName);

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

            logger.info("Connect Server [{}] at location {}", serverName, location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
