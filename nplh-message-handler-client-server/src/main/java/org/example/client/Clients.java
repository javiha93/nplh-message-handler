package org.example.client;

import lombok.Getter;
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

    public void updateClients(HostInfoList hostInfoList) {
        List<HostInfo> hl7Hosts = hostInfoList.getHL7Hosts();
        List<HostInfo> missingHl7Hosts = new ArrayList<>();
        for (HostInfo hostInfo : hl7Hosts) {
            boolean clientDoesNotExist = clientList.stream()
                    .noneMatch(client -> client.clientName.equals(hostInfo.getHostName()));
            if (clientDoesNotExist) {
                missingHl7Hosts.add(hostInfo);
            }
        }
        addHL7Clients(missingHl7Hosts);

        List<HostInfo> wsHosts = hostInfoList.getWSHosts();
        List<HostInfo> missingWsHosts = new ArrayList<>();
        for (HostInfo hostInfo : wsHosts) {
            boolean clientDoesNotExist = clientList.stream()
                    .noneMatch(client -> client.clientName.equals(hostInfo.getHostName()));
            if (clientDoesNotExist) {
                missingWsHosts.add(hostInfo);
            }
        }
        addWSClients(missingWsHosts);

        List<Client> clientsToDelete = new ArrayList<>();
        for (Client client : clientList) {
            boolean hostDoesNotExist = hostInfoList.getHostInfos().stream()
                    .noneMatch(hostInfo -> hostInfo.getHostName().equals(client.getClientName()));
            if (hostDoesNotExist) {
                clientsToDelete.add(client);
            }
        }
        clientList.removeAll(clientsToDelete);
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
