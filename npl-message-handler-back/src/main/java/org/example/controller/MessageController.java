package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.Clients;
import org.example.Host;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final Clients clients;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
        this.clients = new Clients();
    }

    @PostMapping("/generate")
    public ResponseEntity<Message> generateMessage(@RequestBody SampleIdRequest request) {
        String sampleId = (request != null && request.getSampleId() != null) ? request.getSampleId() : "";
        Message message = messageService.generateMessage(sampleId);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody SendMessageRequest request) {
        try {
            String message = request.message;
            String hostName = request.hostName;
            String messageType = request.messageType;
            
            // Convert string host to Host enum
            Host host = Host.valueOf(hostName);
            
            // Here you would implement the actual sending logic
            // For now, just log the received data
            System.out.println("Sending message to host: " + host.name());
            System.out.println("Message type: " + messageType);
            System.out.println("Message content: " + message);
            
            return ResponseEntity.ok("Message sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid host: " + request.hostName);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending message: " + e.getMessage());
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convertMessage(@RequestBody UnifiedConvertRequest request) {
        String convertedMessage;
        
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
        private String hostName;
        private String messageType;
    }
}
