package org.example.server;

import lombok.Getter;
import org.example.client.Clients;
import org.example.domain.host.HostType;
import org.example.domain.host.Connection;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.server.impl.LISHandler;
import org.example.server.impl.VTGHandler;
import org.example.service.IrisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Servers {
    List<Server> serverList = new ArrayList<>();
    IrisService irisService;
    Clients clients;

    static final Logger logger = LoggerFactory.getLogger(Servers.class);

    public Servers(HostInfoList hostInfoList, IrisService irisService, Clients clients) {
        this.irisService = irisService;
        //TO DO: Needed for VTGWS only check better approach
        this.clients = clients;

        logger.info("****************************************************************************************************");
        logger.info("***                                    ENABLING SERVERS                                          ***");

        addHL7Servers(hostInfoList.getHL7Hosts());
        addWSServers(hostInfoList.getWSHosts());

        logger.info("****************************************************************************************************");

    }

    private void addHL7Servers(List<HostInfo> hl7Servers) {
        for (HostInfo host: hl7Servers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                // TO DO enum for hostType
                 if (host.getHostType().equals(HostType.LIS)) { //   if (connection.getWsName() == null) {
                    serverList.add(new LISHandler(host.getHostName(), host.getHostType(), connection, irisService));
                }
                 if (host.getHostType().equals(HostType.VTG) && !connection.getConnectionName().contains("VIP") && !connection.getConnectionName().contains("WS")) {
                     serverList.add(new VTGHandler(host.getHostName(), host.getHostType(), connection, irisService));
                 }
            }
        }
    }

    // TO DO check if
    private void addWSServers(List<HostInfo> wsServers) {
        for (HostInfo host: wsServers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                serverList.add(new WSServer(host.getHostName(), host.getHostType(), connection, irisService, clients));
            }
        }
    }

    public Server getServerByName(String serverName) {
        for (Server server : serverList) {
            if (server.getServerName().equals(serverName)) {
                return  server;
            }
        }
        throw new RuntimeException("Not found server");
    }
}
