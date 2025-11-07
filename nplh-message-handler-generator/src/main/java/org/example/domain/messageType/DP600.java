package org.example.domain.messageType;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.DP600.DP600ToNPLH.SendScannedImageLabelId.DP600_SendScannedSlideImageLabelId;
import org.example.domain.ws.DP600.DP600ToNPLH.SendUpdatedSlideStatus.DP600_SendUpdatedSlideStatus;
import org.example.service.MessageService;

public enum DP600 implements MessageType {
    sendUpdatedSlideStatus {
        public MessageService.MessageResponse convert(Message m, Slide slide, String status) {
            DP600_SendUpdatedSlideStatus sendUpdatedSlideStatus = DP600_SendUpdatedSlideStatus.FromMessage(slide, status);
            return new MessageService.MessageResponse(sendUpdatedSlideStatus.toString(), "");
        }
    },
    sendScannedSlideImageLabelId {
        public MessageService.MessageResponse convert(Message m, Slide slide) {
            DP600_SendScannedSlideImageLabelId sendScannedSlide = DP600_SendScannedSlideImageLabelId.FromMessage(slide);
            return new MessageService.MessageResponse(sendScannedSlide.toString(), "");
        }
    }
}
