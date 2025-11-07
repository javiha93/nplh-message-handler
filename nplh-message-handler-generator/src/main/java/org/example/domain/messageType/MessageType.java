package org.example.domain.messageType;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.service.MessageService;

public interface MessageType {
    default MessageService.MessageResponse convert(Message message) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Specimen specimen) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Block block) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Slide slide) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Slide slide, String status) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Block block, String status) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, Specimen specimen, String status) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
    default MessageService.MessageResponse convert(Message message, String status) {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
}
