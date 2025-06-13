package org.example.server;

import org.example.domain.host.HL7Host;

import java.util.ArrayList;
import java.util.List;

public class Servers {
    List<Server> serverList = new ArrayList<>();

    public Servers() {
        HL7Server lisServer = new HL7Server(HL7Host.LIS);

        serverList.add(lisServer);
    }
}
