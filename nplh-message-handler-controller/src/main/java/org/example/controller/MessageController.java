package org.example.controller;

import org.example.domain.UnifiedConvertRequest;
import lombok.Getter;
import lombok.Setter;
import org.example.client.*;
import org.example.domain.client.message.ClientMessageResponse;
import org.example.domain.message.Message;
import org.example.server.Servers;
import org.example.service.DuplicateHostService;
import org.example.service.IrisService;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.domain.messageType.MessageTypeResolver.fromString;


@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final IrisService irisService;
    private final Clients clients;

    @Autowired
    public MessageController(MessageService messageService, IrisService irisService, Clients clients, Servers servers) {
        this.messageService = messageService;
        this.irisService = irisService;
        this.clients = clients;
    }

    @PostMapping("/generate")
    public ResponseEntity<Message> generateMessage(@RequestBody SampleIdRequest request) {
        String sampleId = (request != null && request.getSampleId() != null) ? request.getSampleId() : "";
        Message message = messageService.generateMessage(sampleId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/send")
    public ResponseEntity<List<ClientMessageResponse>> sendMessage(@RequestBody SendMessageRequest request) {
        List<ClientMessageResponse> response = new ArrayList<>();

        Client client = clients.getClient(request.hostName);

        if (client instanceof HL7Client) {
            ((HL7Client) client).send(request.message, request.controlId);
       } else if (client instanceof WSClient) {
            String result = ((WSClient) client).send(request.messageType, request.message, request.controlId);
            response = Collections.singletonList(new ClientMessageResponse(result));
        } else if (client instanceof RestClient) {
            String result = ((RestClient) client).send(request.messageType, request.message, request.controlId);
            response = Collections.singletonList(new ClientMessageResponse(result));
        } else {
            throw new RuntimeException("Not found client instance");
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
    public ResponseEntity<MessageService.MessageResponse> convertMessage(@RequestBody UnifiedConvertRequest request) throws Exception {
        MessageService.MessageResponse convertedMessage;

        if (request.getSlide() != null && request.getStatus() != null) {
            // Case for slide with status
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getSlide(),
                    request.getStatus()
            );
        } else if (request.getBlock() != null && request.getStatus() != null) {
            // Case for block with status
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getBlock(),
                    request.getStatus()
            );
        } else if (request.getSpecimen() != null && request.getStatus() != null) {
            // Case for specimen with status
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getSpecimen(),
                    request.getStatus()
            );
        } else if (request.getSlide() != null) {
            // Case for slide only
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getSlide()
            );
        } else if (request.getSpecimen() != null) {
            // Case for specimen
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getSpecimen()
            );
        } else if (request.getBlock() != null) {
            // Case for specimen
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getBlock()
            );
        } else if (request.getStatus() != null) {
            // Case for status only
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType()),
                    request.getStatus()
            );
        } else {
            // Basic case - message and type only
            convertedMessage = messageService.convertMessage(
                    request.getMessage(),
                    fromString(request.getMessageType(), request.getHostType())
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
        private String hostName;
        private String hostType;
        private String messageType;
        private String controlId;
    }
}
