
package org.example.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE.LIS_CASEUPDATE;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.messageType.*;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public Message generateMessage(String sampleId) {
        return Message.Default(sampleId);
    }

    public Message generateMessage() {
        return Message.Default("");
    }

    public MessageResponse convertMessage(Message message, MessageType messageType) {
        return messageType.convert(message);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Specimen specimen) {
        return messageType.convert(message, specimen);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Block block) {
        return messageType.convert(message, block);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Slide slide) {
        return messageType.convert(message, slide);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Slide slide, String status) {
        return messageType.convert(message, slide, status);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Block block, String status) {
        return messageType.convert(message, block, status);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, Specimen specimen, String status) {
        return messageType.convert(message, specimen, status);
    }

    public MessageResponse convertMessage(Message message, MessageType messageType, String status) {
        return messageType.convert(message, status);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
        private String controlId;
    }
}
