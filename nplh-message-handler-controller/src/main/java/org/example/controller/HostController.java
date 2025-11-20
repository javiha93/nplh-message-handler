package org.example.controller;

import lombok.Data;
import org.example.client.Client;
import org.example.client.Clients;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.server.message.response.ResponseInfo;
import org.example.domain.server.message.response.ResponseStatus;
import org.example.domain.server.message.ServerMessage;
import org.example.server.Server;
import org.example.server.Servers;
import org.example.service.DuplicateHostService;
import org.example.service.IrisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.service.IrisService.parseList;

@RestController
@RequestMapping("/api/hosts")
@CrossOrigin(origins = "*")
public class HostController {

    private static final Logger logger = LoggerFactory.getLogger(HostController.class);

    private final IrisService irisService;
    private final DuplicateHostService duplicateHostService;
    private final Servers servers;
    private final Clients clients;

    @Autowired
    public HostController(IrisService irisService, DuplicateHostService duplicateHostService, Clients clients, Servers servers) {
        this.irisService = irisService;
        this.duplicateHostService = duplicateHostService;
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

    @PostMapping("/updateClientsServers")
    public ResponseEntity<String> updateServersClients() {
        try {
            HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));

            clients.updateClients(hostInfoList);
            servers.updateServers(hostInfoList);

            return ResponseEntity.ok("Updated servers and clients successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error duplicating host: " + e.getMessage());
        }
    }

    @PostMapping("/duplicate")
    public ResponseEntity<String> duplicateHost(String hostName, String newHostName) {
        try {
            duplicateHostService.duplicateHost(hostName, List.of(newHostName));
            return ResponseEntity.ok("Duplicate host " + hostName + " successfully. New host: " + newHostName );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error duplicating host: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteHost(String hostName) {
        try {
            duplicateHostService.deleteHost(hostName);
            return ResponseEntity.ok("Deleted host " + hostName + " successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error removing host: " + e.getMessage());
        }
    }

    
    private ServerDTO convertToDTO(Server server) {
        ServerDTO dto = new ServerDTO();
        dto.serverName = server.getServerName();
        dto.responses = server.getResponses();
        dto.isRunning = server.getIsRunning();
        dto.isDefault = server.getIsDefault();
        dto.messages = server.getMessages();

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

    @PostMapping("servers/modifyServer")
    public ResponseEntity<ServerDTO> modifyServer(@RequestBody ServerDTO serverInfo) {
        try {
            // ✨ Debug logging
            logger.info("🔍 Received modifyServer request:");
            logger.info("  serverName: {}", serverInfo.serverName);
            logger.info("  isRunning: {}", serverInfo.getIsRunning());
            logger.info("  responses count: {}", serverInfo.getResponses() != null ? serverInfo.getResponses().size() : 0);

            if (serverInfo.getResponses() != null) {
                for (int i = 0; i < serverInfo.getResponses().size(); i++) {
                    ResponseInfo response = serverInfo.getResponses().get(i);
                    logger.info("  response[{}]: messageType={}, isDefault={}",
                            i, response.getMessageType(), response.getIsDefault());
                }
            }

            Server server = servers.getServerByName(serverInfo.serverName);

            if (server == null) {
                logger.error("❌ Server not found: {}", serverInfo.serverName);
                return ResponseEntity.notFound().build();
            }

            // ✨ Actualizar el estado de ejecución del servidor
            server.setIsRunning(serverInfo.getIsRunning());
            server.setMessages(serverInfo.getMessages());

            // ✨ Actualizar todas las respuestas, no solo la default
            if (serverInfo.getResponses() != null && !serverInfo.getResponses().isEmpty()) {

                // Iterar sobre las respuestas recibidas del frontend
                for (ResponseInfo incomingResponse : serverInfo.getResponses()) {

                    // Buscar la respuesta correspondiente en el servidor por messageType
                    ResponseInfo existingResponse = server.getResponses().stream()
                            .filter(r -> r.getMessageType().equals(incomingResponse.getMessageType()))
                            .findFirst()
                            .orElse(null);

                    if (existingResponse != null) {
                        // ✨ Actualizar la respuesta existente
                        logger.info("🔄 Updating existing response: {}", incomingResponse.getMessageType());

                        existingResponse.setIsDefault(incomingResponse.getIsDefault());

                        // Actualizar applicationResponse si no es null
                        if (incomingResponse.getApplicationResponse() != null) {
                            existingResponse.setApplicationResponse(incomingResponse.getApplicationResponse());
                            logger.info("  ✅ Updated applicationResponse for {}", incomingResponse.getMessageType());
                        } else {
                            logger.warn("  ⚠️ applicationResponse is null for {}, keeping existing value", incomingResponse.getMessageType());
                        }

                        // Actualizar communicationResponse si no es null
                        if (incomingResponse.getCommunicationResponse() != null) {
                            existingResponse.setCommunicationResponse(incomingResponse.getCommunicationResponse());
                            logger.info("  ✅ Updated communicationResponse for {}", incomingResponse.getMessageType());
                        } else {
                            logger.warn("  ⚠️ communicationResponse is null for {}, keeping existing value", incomingResponse.getMessageType());
                        }

                    } else {
                        // ✨ Agregar nueva respuesta si no existe
                        logger.info("➕ Adding new response: {}", incomingResponse.getMessageType());

                        // Validar que los campos requeridos no sean null
                        if (incomingResponse.getApplicationResponse() == null) {
                            incomingResponse.setApplicationResponse(ResponseStatus.disabled());
                            logger.warn("  ⚠️ Creating default applicationResponse for new response");
                        }

                        if (incomingResponse.getCommunicationResponse() == null) {
                            incomingResponse.setCommunicationResponse(ResponseStatus.disabled());
                            logger.warn("  ⚠️ Creating default communicationResponse for new response");
                        }

                        server.addResponse(incomingResponse);
                    }
                }

                // ✨ Asegurar que solo una respuesta esté marcada como default
                long defaultCount = server.getResponses().stream()
                        .mapToLong(r -> r.getIsDefault() ? 1 : 0)
                        .sum();

                if (defaultCount == 0) {
                    // Si no hay default, hacer la primera como default
                    if (!server.getResponses().isEmpty()) {
                        server.getResponses().get(0).setIsDefault(true);
                        logger.info("🔧 Set first response as default: {}", server.getResponses().get(0).getMessageType());
                    }
                } else if (defaultCount > 1) {
                    // Si hay múltiples defaults, mantener solo el primero
                    boolean foundFirst = false;
                    for (ResponseInfo response : server.getResponses()) {
                        if (response.getIsDefault() && !foundFirst) {
                            foundFirst = true;
                            logger.info("🔧 Keeping {} as default", response.getMessageType());
                        } else if (response.getIsDefault()) {
                            response.setIsDefault(false);
                            logger.info("🔧 Removed default from {}", response.getMessageType());
                        }
                    }
                }

            } else {
                logger.warn("⚠️ No responses provided in request, keeping server's existing responses");
            }

            // ✨ Log final state
            logger.info("✅ Server {} updated successfully. Final state:", serverInfo.serverName);
            logger.info("  isRunning: {}", server.getIsRunning());
            server.getResponses().forEach(r ->
                    logger.info("  response: {} (default: {})", r.getMessageType(), r.getIsDefault())
            );

            // Convert to DTO and return
            ServerDTO dto = convertToDTO(server);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            logger.error("❌ Error modifying server: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("servers/addMessage")
    public ResponseEntity<ServerDTO> addMessageToServer(@RequestBody AddMessageRequest request) {
        try {
            logger.info("📬 Adding messages to server '{}'", request.serverName);
            
            Server server = servers.getServerByName(request.serverName);
            
            if (server == null) {
                logger.error("❌ Server not found: {}", request.serverName);
                return ResponseEntity.notFound().build();
            }
            
            // Get current messages list
            List<ServerMessage> currentMessages = server.getMessages();
            if (currentMessages == null) {
                currentMessages = new ArrayList<>();
                server.setMessages(currentMessages);
            }
            
            // Add new messages from responses
            if (request.responses != null && !request.responses.isEmpty()) {
                for (ServerMessage response : request.responses) {
                    currentMessages.add(response);
                    logger.debug("  ➕ Added response message");
                }
                logger.info("✅ Added {} messages to server '{}'", request.responses.size(), request.serverName);
            }
            
            // Convert to DTO and return
            ServerDTO dto = convertToDTO(server);
            return ResponseEntity.ok(dto);
            
        } catch (Exception e) {
            logger.error("❌ Error adding message to server: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /*
    // DEPRECATED: Ahora se usa modifyServer para toggle functionality
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
    */
    
    @Data
    public static class AddMessageRequest {
        public String serverName;
        public List<ServerMessage> responses;
    }
    
    @Data
    public static class ServerDTO {
        public String serverName;
        public Boolean isDefault;
        public Boolean isRunning;
        public List<ResponseInfo> responses;
        public String hostType;
        public String location;
        public List<ServerMessage> messages = new ArrayList<>();


        public ResponseInfo getDefaultResponse() {
            return responses.stream()
                    .filter(ResponseInfo::getIsDefault)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Not found default response"));
        }
    }
}
