package org.example.server;

import lombok.Getter;
import org.example.client.Clients;
import org.example.domain.host.host.Connection;
import org.example.domain.host.host.HostInfo;
import org.example.domain.host.host.HostInfoList;
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
        this.clients = clients;

        logger.info("****************************************************************************************************");
        logger.info("***                                    ENABLING SERVERS                                          ***");

//        HL7Server lisServer = new HL7Server(HL7Host.LIS, irisService);
//        serverList.add(lisServer);
        addHL7Servers(hostInfoList.getHL7Hosts());
        addWSServers(hostInfoList.getWSHosts());

        logger.info("****************************************************************************************************");

    }

    private void addHL7Servers(List<HostInfo> hl7Servers) {
        for (HostInfo host: hl7Servers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                 if (host.getHostName().equals("LIS_HL7")) { //   if (connection.getWsName() == null) {
                    serverList.add(new LISHandler(host.getHostName(), connection, irisService));
                }
                 if (host.getHostType().equals("VTG") && !connection.getConnectionName().contains("VIP") && !connection.getConnectionName().contains("WS")) {
                     serverList.add(new VTGHandler(host.getHostName(), connection, irisService));
                 }
            }
        }
    }

    private void addWSServers(List<HostInfo> wsServers) {
        for (HostInfo host: wsServers) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                serverList.add(new WSServer(host.getHostName(), host.getHostType(), connection, irisService, clients));
            }
        }
    }

    public Server getServerByName(String serverName) throws Exception {
        for (Server server : serverList) {
            if (server.getServerName().equals(serverName)) {
                return  server;
            }
        }
        throw new Exception("Not found server");
    }
}
