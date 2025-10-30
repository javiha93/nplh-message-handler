package controller;

import lombok.Data;
import org.example.client.Client;
import org.example.client.Clients;
import org.example.domain.ResponseStatus;
import org.example.domain.host.host.HostInfo;
import org.example.domain.host.host.HostInfoList;
import org.example.server.Server;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hosts")
@CrossOrigin(origins = "*")
public class HostController {

    private final IrisService irisService;
    private final Servers servers;
    private final Clients clients;

    @Autowired
    public HostController(IrisService irisService, Clients clients, Servers servers) {
        this.irisService = irisService;
        this.servers = servers;
        this.clients = clients;
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getHostsClients() {
        return ResponseEntity.ok(clients.getClientList());
    }

    @GetMapping("/servers")
    public ResponseEntity<List<ServerDTO>> getHostsServers() {
        try {
            List<Server> serverList = servers.getServerList();
            List<ServerDTO> serverDTOs = serverList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(serverDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    
    private ServerDTO convertToDTO(Server server) {
        ServerDTO dto = new ServerDTO();
        dto.serverName = server.getServerName();
        dto.applicationResponse = server.getApplicationResponse();
        dto.communicationResponse = server.getCommunicationResponse();
        dto.isRunning = server.getIsRunning();
        
        // Check if it's a WSServer using reflection to get additional properties
        try {
            if (server.getClass().getSimpleName().equals("WSServer")) {
                // Use reflection to get hostType and location
                dto.hostType = (String) server.getClass().getMethod("getHostType").invoke(server);
                dto.location = (String) server.getClass().getMethod("getLocation").invoke(server);
            }
        } catch (Exception e) {
            // Ignore reflection errors
        }
        
        return dto;
    }

    @PostMapping("/servers/toggle")
    public ResponseEntity<ServerDTO> toggleServer(@RequestBody ToggleServerRequest request) {
        try {
            Server server = servers.getServerByName(request.serverName);
            
            if (server == null) {
                return ResponseEntity.notFound().build();
            }
            // Toggle the server running state
            boolean newState = !server.getIsRunning();
            server.setIsRunning(newState);
            
            // Convert to DTO and return
            ServerDTO dto = convertToDTO(server);
            return ResponseEntity.ok(dto);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Data
    public static class ToggleServerRequest {
        public String serverName;
    }
    
    @Data
    public static class ServerDTO {
        public String serverName;
        public Boolean isRunning;
        public ResponseStatus applicationResponse;
        public ResponseStatus communicationResponse;
        public String hostType;
        public String location;

    }
}
