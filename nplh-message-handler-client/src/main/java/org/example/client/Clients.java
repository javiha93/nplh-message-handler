package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.example.domain.host.HL7Host;
import org.example.domain.host.Host;
import org.example.domain.host.RESTHost;
import org.example.domain.host.WSHost;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clients {

    String installationPath;
    List<Client> clientList = new ArrayList<>();

    public Clients(String installationPath) {
        this.installationPath = new File(installationPath).getParent();

        HL7Client lis = new HL7Client(HL7Host.LIS);
        HL7Client vtg = new HL7Client(HL7Host.VTG);
        WSClient upathCloud = new WSClient(WSHost.UPATH_CLOUD);
        WSClient vtgWs = new WSClient(WSHost.VANTAGE_WS);
        //RestClient restClient = new RestClient(RESTHost.AUTOMATION_SW);

        for (RESTHost restHost: getRestHostInfo()) {
            RestClient restClient = new RestClient(restHost);
            clientList.add(restClient);
        }

        clientList.add(lis);
        clientList.add(vtg);
        clientList.add(upathCloud);
        clientList.add(vtgWs);
        //clientList.add(restClient);
    }

    private List<RESTHost> getRestHostInfo() {
        String ocYml = installationPath + "\\OpenConnect\\application.yml";

        File ymlFile = new File(ocYml);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        JsonNode root = null;
        try {
            root = mapper.readTree(ymlFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<RESTHost> restHosts = new ArrayList<>();
        JsonNode devices = root.path("automation").path("devices");

        if (devices.isArray()) {
            for (JsonNode device : devices) {
                String name = device.path("name").asText();
                String apiKey = device.path("apiKeyValue").asText();
                restHosts.add(new RESTHost(name, apiKey));
            }
        }

        return restHosts;
    }

    public Client getClient(Host host) {
        for (Client client: clientList) {
            if (Objects.equals(client.clientName, host.name())) {
                return client;
            }
        }
        return null;
    }
}
