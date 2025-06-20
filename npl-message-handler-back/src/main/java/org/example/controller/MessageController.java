package org.example.controller;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.example.client.Client;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.client.message.ClientMessageResponse;
import org.example.domain.host.Host;
import org.example.domain.host.HostDeserializer;
import org.example.domain.message.Message;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final IrisService irisService;
    private final Clients clients;

    private final Servers servers;

    @Autowired
    public MessageController(MessageService messageService, IrisService irisService) {
        this.messageService = messageService;
        this.irisService = irisService;
        this.clients = new Clients(irisService.getHostInfo());
        this.servers = new Servers();
    }

    @PostMapping("/generate")
    public ResponseEntity<Message> generateMessage(@RequestBody SampleIdRequest request) {
        String sampleId = (request != null && request.getSampleId() != null) ? request.getSampleId() : "";
        Message message = messageService.generateMessage(sampleId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/send")
    public ResponseEntity<List<ClientMessageResponse>> sendMessage(@RequestBody SendMessageRequest request) {
        List<ClientMessageResponse> response;

        Client client = clients.getClient(request.hostName);

        if (client instanceof HL7Client) {
            response = client.send(request.message, request.controlId);
        } else if (client instanceof WSClient) {
            response = Collections.singletonList(new ClientMessageResponse(client.send(request.messageType, request.message, request.controlId)));
        } else {
            response = Collections.singletonList(new ClientMessageResponse(client.send(request.messageType, request.message, request.controlId)));
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteAll")
    public ResponseEntity<String> deleteAllMessages() {
        try {
            irisService.deleteAllMessages();
            return ResponseEntity.ok("Todos los mensajes han sido eliminados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar mensajes: " + e.getMessage());
        }
    }


    @PostMapping("/convert")
    public ResponseEntity<MessageService.MessageResponse> convertMessage(@RequestBody UnifiedConvertRequest request) {
        MessageService.MessageResponse convertedMessage;
        
        if (request.getSlide() != null && request.getStatus() != null) {
            // Case for slide with status
            convertedMessage = messageService.convertMessage(
                request.getMessage(), 
                request.getMessageType(), 
                request.getSlide(), 
                request.getStatus()
            );
        } else if (request.getBlock() != null && request.getStatus() != null) {
            // Case for block with status
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    request.getMessageType(),
                    request.getBlock(),
                    request.getStatus()
            );
        } else if (request.getSpecimen() != null && request.getStatus() != null) {
            // Case for specimen with status
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    request.getMessageType(),
                    request.getSpecimen(),
                    request.getStatus()
            );
        } else if (request.getSlide() != null) {
            // Case for slide only
            convertedMessage = messageService.convertMessage(
                request.getMessage(), 
                request.getMessageType(), 
                request.getSlide()
            );
        } else if (request.getSpecimen() != null) {
            // Case for specimen
            convertedMessage = messageService.convertMessage(
                request.getMessage(), 
                request.getMessageType(), 
                request.getSpecimen()
            );
        } else if (request.getStatus() != null) {
            // Case for status only
            convertedMessage = messageService.convertMessage(
                request.getMessage(), 
                request.getMessageType(), 
                request.getStatus()
            );
        } else {
            // Basic case - message and type only
            convertedMessage = messageService.convertMessage(
                request.getMessage(), 
                request.getMessageType()
            );
        }
        
        return ResponseEntity.ok(convertedMessage);
    }

    @Setter
    @Getter
    public static class SampleIdRequest {
        private String sampleId;
    }

    @Setter
    @Getter
    public static class SendMessageRequest {
        private String message;
        @JsonDeserialize(using = HostDeserializer.class)
        private Host hostName;
        private String messageType;
        private String controlId;
    }
}
