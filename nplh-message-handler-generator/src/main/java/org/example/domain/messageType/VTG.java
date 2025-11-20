package org.example.domain.messageType;

import org.example.domain.hl7.VTG.VTGToNPLH.ADDITION.VTG_ADDITION;
import org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE.VTG_BlockUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.OEWF.VTG_OEWF;
import org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE.VTG_SlideUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE.VTG_SpecimenUpdate;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.service.MessageService;

public enum VTG implements MessageType {
    OEWF {
        public MessageService.MessageResponse convert(Message m) {
            VTG_OEWF oewf = VTG_OEWF.FromMessage(m);
            return new MessageService.MessageResponse(oewf.toString(), oewf.getControlId());
        }
    },
    ADDITION {
        public MessageService.MessageResponse convert(Message m) {
            VTG_ADDITION addition = VTG_ADDITION.FromMessage(m);
            return new MessageService.MessageResponse(addition.toString(), addition.getControlId());
        }
    },
    SLIDE_UPDATE {
        public MessageService.MessageResponse convert(Message m, Slide slide, String status) {
            VTG_SlideUpdate slideUpdate = VTG_SlideUpdate.fromMessage(m, slide, status);
            return new MessageService.MessageResponse(slideUpdate.toString(), slideUpdate.getControlId());
        }
    },
    BLOCK_UPDATE {
        public MessageService.MessageResponse convert(Message m, Block block, String status) {
            VTG_BlockUpdate blockUpdate = VTG_BlockUpdate.FromMessage(m, block, status);
            return new MessageService.MessageResponse(blockUpdate.toString(), blockUpdate.getControlId());
        }
    },
    SPECIMEN_UPDATE {
        public MessageService.MessageResponse convert(Message m, Specimen specimen, String status) {
            VTG_SpecimenUpdate specimenUpdate = VTG_SpecimenUpdate.FromMessage(m, specimen, status);
            return new MessageService.MessageResponse(specimenUpdate.toString(), specimenUpdate.getControlId());
        }
    }
}
