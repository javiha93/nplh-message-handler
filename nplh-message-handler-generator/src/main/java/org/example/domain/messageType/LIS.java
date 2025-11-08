package org.example.domain.messageType;

import org.example.domain.hl7.LIS.LISToNPLH.ADTA08.LIS_ADT_A08;
import org.example.domain.hl7.LIS.LISToNPLH.ADTA28.LIS_ADT_A28;
import org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE.LIS_CASEUPDATE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETECASE.LIS_DELETE_CASE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE.LIS_DELETE_SLIDE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESPECIMEN.LIS_DELETE_SPECIMEN;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.service.MessageService;


public enum LIS implements MessageType {
    OML21 {
        public MessageService.MessageResponse convert(Message m) {
            LIS_OML21 oml21 = LIS_OML21.fromMessage(m);
            return new MessageService.MessageResponse(oml21.toString(), oml21.getControlId());
        }
    },
    DELETE_SLIDE {
        public MessageService.MessageResponse convert(Message m) {
            LIS_DELETE_SLIDE deleteSlide = LIS_DELETE_SLIDE.FromMessage(m, new Slide());
            return new MessageService.MessageResponse(deleteSlide.toString(), deleteSlide.getControlId());
        }

        public MessageService.MessageResponse convert(Message m, Slide slide) {
            LIS_DELETE_SLIDE deleteSlide = LIS_DELETE_SLIDE.FromMessage(m, slide);
            return new MessageService.MessageResponse(deleteSlide.toString(), deleteSlide.getControlId());
        }
    },
    ADTA28 {
        public MessageService.MessageResponse convert(Message m) {
            LIS_ADT_A28 adta28 = LIS_ADT_A28.FromMessage(m);
            return new MessageService.MessageResponse(adta28.toString(), adta28.getControlId());
        }
    },
    ADTA08 {
        public MessageService.MessageResponse convert(Message m) {
            LIS_ADT_A08 adta08 = LIS_ADT_A08.FromMessage(m);
            return new MessageService.MessageResponse(adta08.toString(), adta08.getControlId());
        }
    },
    DELETE_CASE {
        public MessageService.MessageResponse convert(Message m) {
            LIS_DELETE_CASE deletecase = LIS_DELETE_CASE.FromMessage(m);
            return new MessageService.MessageResponse(deletecase.toString(), deletecase.getControlId());
        }
    },
    DELETE_SPECIMEN {
        public MessageService.MessageResponse convert(Message m, Specimen specimen) {
            LIS_DELETE_SPECIMEN deletespecimen = LIS_DELETE_SPECIMEN.FromMessage(m, specimen);
            return new MessageService.MessageResponse(deletespecimen.toString(), deletespecimen.getControlId());
        }
    },
    CASE_UPDATE {
        public MessageService.MessageResponse convert(Message m, String status) {
            LIS_CASEUPDATE caseupdate = LIS_CASEUPDATE.FromMessage(m, status);
            return new MessageService.MessageResponse(caseupdate.toString(), caseupdate.getControlId());
        }
    }
}
