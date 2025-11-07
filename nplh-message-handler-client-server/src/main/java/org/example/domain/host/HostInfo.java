package org.example.domain.host;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HostInfo {
    private String hostId;
    private String hostName;
    private String hostType;
    private ConnectionType connectionType;
    private List<Connection> connections;

    public List<Connection> getInboundConnections() {
        List<Connection> inboundConnections = new ArrayList<>();
        for (Connection connection: connections) {
            if (connection.getDirection().equals(Direction.IN.name())) {
                inboundConnections.add(connection);
            }
        }
        return inboundConnections;
    }

    public List<Connection> getOutboundConnections() {
        List<Connection> outboundConnections = new ArrayList<>();
        for (Connection connection: connections) {
            if (connection.getDirection().equals(Direction.OUT.name())) {
                outboundConnections.add(connection);
            }
        }
        return outboundConnections;
    }
}
