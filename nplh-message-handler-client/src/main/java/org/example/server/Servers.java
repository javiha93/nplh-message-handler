package org.example.server;

import org.example.domain.host.HL7Host;
import org.example.domain.host.host.Connection;
import org.example.domain.host.host.HostInfo;
import org.example.domain.host.host.HostInfoList;

import java.util.ArrayList;
import java.util.List;

public class Servers {
    List<Server> serverList = new ArrayList<>();

    public Servers(HostInfoList hostInfoList) {
        HL7Server lisServer = new HL7Server(HL7Host.LIS);
        addWSServers(hostInfoList.getWSHosts());

        serverList.add(lisServer);

    }

    private void addWSServers(List<HostInfo> wsClients) {
        for (HostInfo host: wsClients) {
            List<Connection> outboundConnections = host.getOutboundConnections();
            for (Connection connection: outboundConnections) {
                serverList.add(new WSServer(host.getHostName(), connection.getWsLocation()));
            }
        }
    }
}
