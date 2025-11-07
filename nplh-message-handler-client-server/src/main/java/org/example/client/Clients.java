package org.example.client;

import lombok.Getter;
import org.example.domain.client.Client;
import org.example.domain.host.Connection;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.service.IrisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Clients {

    IrisService irisService;
    List<Client> clientList = new ArrayList<>();

    static final Logger logger = LoggerFactory.getLogger(Clients.class);

    public Clients(HostInfoList hostInfoList, IrisService irisService) {
        this.irisService = irisService;

        logger.info("****************************************************************************************************");
        logger.info("***                                    ENABLING CLIENTS                                          ***");

        addHL7Clients(hostInfoList.getHL7Hosts());
        addWSClients(hostInfoList.getWSHosts());
        addAutomationClients(hostInfoList.getAutomationSWHosts());

        logger.info("****************************************************************************************************");
    }

    private void addHL7Clients(List<HostInfo> hl7Hosts) {
        for (HostInfo host: hl7Hosts) {
            List<Connection> inboundConnections = host.getInboundConnections();
            for (Connection connection: inboundConnections) {
                clientList.add(new HL7Client(host.getHostName(), host.getHostType(), connection, irisService));
            }
        }
    }

    private void addWSClients(List<HostInfo> wsClients) {
        for (HostInfo host: wsClients) {
            List<Connection> inboundConnections = host.getInboundConnections();
            for (Connection connection: inboundConnections) {
                clientList.add(new WSClient(host.getHostName(), host.getHostType(), connection, irisService));
            }
        }
    }

    private void addAutomationClients(List<HostInfo> automationHosts) {
        for (HostInfo host: automationHosts) {
            List<Connection> inboundConnections = host.getInboundConnections();
            for (Connection connection: inboundConnections) {
                clientList.add(new RestClient(host.getHostName(), host.getHostType(), connection, irisService));
            }
        }
    }

//    private List<RESTHost> getRestHostInfo() {
//        String ocYml = installationPath + "\\OpenConnect\\application.yml";
//
//        File ymlFile = new File(ocYml);
//
//        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//        JsonNode root;
//        try {
//            root = mapper.readTree(ymlFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        List<RESTHost> restHosts = new ArrayList<>();
//        JsonNode devices = root.path("automation").path("devices");
//
//        if (devices.isArray()) {
//            for (JsonNode device : devices) {
//                String name = device.path("name").asText();
//                String apiKey = device.path("apiKeyValue").asText();
//                restHosts.add(new RESTHost(name, apiKey));
//            }
//        }
//
//        return restHosts;
//    }

    public Client getClient(String host) {
        for (Client client: clientList) {
            if (client.getClientName().equals("LIS_HL7") && (host.equals("LIS"))) {
                return client;
            }
            if (Objects.equals(client.getClientName(), host)) {
                return client;
            }
        }
        return null;
    }
}
