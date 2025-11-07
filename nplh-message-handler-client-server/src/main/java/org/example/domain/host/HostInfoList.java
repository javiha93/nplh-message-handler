package org.example.domain.host;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HostInfoList {
    List<HostInfo> hostInfos;

    public List<HostInfo> getAutomationSWHosts() {
        return filterHostsByConnectionType(ConnectionType.AUTOMATION_SW);
    }

    public List<HostInfo> getHL7Hosts() {
        return filterHostsByConnectionType(ConnectionType.HL7);
    }

    public List<HostInfo> getWSHosts() {
        return filterHostsByConnectionType(ConnectionType.WS);
    }

    public List<HostInfo> getDriverEngineHosts() {
        return filterHostsByConnectionType(ConnectionType.DRIVER_ENGINE);
    }

    private List<HostInfo> filterHostsByConnectionType(ConnectionType type) {
        List<HostInfo> host = new ArrayList<>();
        for (HostInfo hostInfo : hostInfos) {
            if (hostInfo.getConnectionType().equals(type)) {
                host.add(hostInfo);
            }
        }
        return host;
    }
}
