package controller;

import org.example.client.Client;
import org.example.client.Clients;
import org.example.domain.host.host.HostInfo;
import org.example.domain.host.host.HostInfoList;
import org.example.server.Server;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List<Server>> getHostsServers() {
        return ResponseEntity.ok(servers.getServerList());
    }
}
