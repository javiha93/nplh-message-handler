package org.example.domain.messageType;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.DP600.DP600ToNPLH.SendScannedImageLabelId.DP600_SendScannedSlideImageLabelId;
import org.example.service.MessageService;

public enum AUTOMATIONSW implements MessageType {
    RETRIEVAL {
        public MessageService.MessageResponse convert(Message m) {
            return new MessageService.MessageResponse(m.getOrder().getSampleId(), null);
        }

        public MessageService.MessageResponse convert(Message m, Specimen specimen) {
            return new MessageService.MessageResponse(specimen.getId(), null);
        }

        public MessageService.MessageResponse convert(Message m, Block block) {
            return new MessageService.MessageResponse(block.getId(), null);
        }

        public MessageService.MessageResponse convert(Message m, Slide slide) {
            return new MessageService.MessageResponse(slide.getId(), null);
        }
    }
}
