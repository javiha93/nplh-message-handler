package org.example;

import java.util.ArrayList;
import java.util.List;

public class Clients {
    List<HL7Client> clientList = new ArrayList<>();

    public Clients() {
        HL7Client hl7Client = new HL7Client(Host.LIS);
        clientList.add(hl7Client);
    }

    public HL7Client getClient(Host host) {
        for (HL7Client client: clientList) {
            if (client.clientName == host.name()) {
                return client;
            }
        }
        return null;
    }
}
