package org.example.domain.messageType;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendReleasedSpecimen.UPATHCLOUD_SendReleasedSpecimen;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendScannedSlide.UPATHCLOUD_SendScannedSlide;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendSlideWSAData.SendSlideWSAData;
import org.example.service.MessageService;

public enum UPATHCLOUD implements MessageType {
    sendReleasedSpecimen {
        public MessageService.MessageResponse convert(Message m, Specimen specimen) {
            UPATHCLOUD_SendReleasedSpecimen sendReleasedSpecimen = UPATHCLOUD_SendReleasedSpecimen.FromSpecimen(m, specimen);
            return new MessageService.MessageResponse(sendReleasedSpecimen.toString(), "");
        }
    },
    sendScannedSlideImageLabelId {
        public MessageService.MessageResponse convert(Message m, Slide slide) {
            UPATHCLOUD_SendScannedSlide sendScannedSlide = UPATHCLOUD_SendScannedSlide.FromMessage(slide);
            return new MessageService.MessageResponse(sendScannedSlide.toString(), "");
        }
    },
    sendSlideWSAData {
        public MessageService.MessageResponse convert(Message m, Slide slide, String status) {
            SendSlideWSAData sendSlideWSAData = SendSlideWSAData.FromMessage(m, slide, status);
            return new MessageService.MessageResponse(sendSlideWSAData.toString(), "");
        }
    }
}
