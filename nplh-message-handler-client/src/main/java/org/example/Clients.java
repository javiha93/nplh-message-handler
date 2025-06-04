package org.example;

import org.example.domain.host.HL7Host;
import org.example.domain.host.Host;
import org.example.domain.host.WSHost;

import java.util.ArrayList;
import java.util.List;

public class Clients {
    List<Client> clientList = new ArrayList<>();

    public Clients() {
        HL7Client lis = new HL7Client(HL7Host.LIS);
        HL7Client vtg = new HL7Client(HL7Host.VTG);
        WSClient upathCloud = new WSClient(WSHost.UPATH_CLOUD);

        clientList.add(lis);
        clientList.add(vtg);
        clientList.add(upathCloud);
    }

    public Client getClient(Host host) {
        for (Client client: clientList) {
            if (client.clientName == host.name()) {
                return client;
            }
        }
        return null;
    }
}
