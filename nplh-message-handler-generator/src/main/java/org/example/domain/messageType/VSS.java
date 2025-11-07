package org.example.domain.messageType;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus.VSS_UpdateSlideStatus;
import org.example.service.MessageService;

public enum VSS implements MessageType {
    UpdateSlideStatus {
        public MessageService.MessageResponse convert(Message m, Slide slide, String status) {
            VSS_UpdateSlideStatus updateSlideStatus = VSS_UpdateSlideStatus.FromSlide(m, slide, status);
            return new MessageService.MessageResponse(updateSlideStatus.toString(), "");
        }
    }
}
