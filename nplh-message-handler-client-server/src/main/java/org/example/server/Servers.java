package org.example.server;

import lombok.Getter;
import org.example.client.Client;
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

    public void updateServers(HostInfoList hostInfoList) {
        List<HostInfo> hl7Hosts = hostInfoList.getHL7Hosts();
        List<HostInfo> missingHl7Hosts = new ArrayList<>();
        for (HostInfo hostInfo : hl7Hosts) {
            boolean serverDoesNotExist = serverList.stream()
                    .noneMatch(server -> server.getServerName().equals(hostInfo.getHostName()));
            if (serverDoesNotExist) {
                missingHl7Hosts.add(hostInfo);
            }
        }
        addHL7Servers(missingHl7Hosts);

        List<HostInfo> wsHosts = hostInfoList.getWSHosts();
        List<HostInfo> missingWsHosts = new ArrayList<>();
        for (HostInfo hostInfo : wsHosts) {
            boolean serverDoesNotExist = serverList.stream()
                    .noneMatch(server -> server.getServerName().equals(hostInfo.getHostName()));
            if (serverDoesNotExist) {
                missingWsHosts.add(hostInfo);
            }
        }
        addWSServers(missingWsHosts);

        List<Server> serversToDelete = new ArrayList<>();
        for (Server server : serverList) {
            boolean hostDoesNotExist = hostInfoList.getHostInfos().stream()
                    .noneMatch(hostInfo -> hostInfo.getHostName().equals(server.getServerName()));
            if (hostDoesNotExist) {
                serversToDelete.add(server);
                server.setIsRunning(false);
            }
        }
        serverList.removeAll(serversToDelete);
    }

    private void addHL7Servers(List<HostInfo> hl7Servers) {
        for (HostInfo host: hl7Servers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                // TO DO enum for hostType
                 if (host.getHostType().equals(HostType.LIS)) { //   if (connection.getWsName() == null) {
                     HL7Server lisServer = new LISHandler(host.getHostName(), host.getHostType(), connection, irisService);
                     lisServer.setIsDefault(host.getIsDefault());
                    serverList.add(lisServer);
                }
                 if (host.getHostType().equals(HostType.VTG) && !connection.getConnectionName().contains("VIP") && !connection.getConnectionName().contains("WS")) {
                     HL7Server vtgServer = new VTGHandler(host.getHostName(), host.getHostType(), connection, irisService);
                     vtgServer.setIsDefault(host.getIsDefault());
                     serverList.add(vtgServer);
                 }
            }
        }
    }

    // TO DO check if
    private void addWSServers(List<HostInfo> wsServers) {
        for (HostInfo host: wsServers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                WSServer wsServer = new WSServer(host.getHostName(), host.getHostType(), connection, irisService, clients);
                wsServer.setIsDefault(host.getIsDefault());
                serverList.add(wsServer);
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
