package org.example.domain.messageType;

import org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE.VTG_SpecimenUpdate;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrder.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrderRequest.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrder.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrderRequest.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrder.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrderRequest.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPatientUpdate.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPhysicianUpdate.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessAssignedPathologistUpdate.*;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessVTGEvent.VTGWS_ProcessVTGEvent;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.response.*;
import org.example.service.MessageService;

public enum VTGWS implements MessageType {
    ProcessPatientUpdate {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessPatientUpdate processPatientUpdate = VTGWS_ProcessPatientUpdate.FromMessage(m);
            return new MessageService.MessageResponse(processPatientUpdate.toString(), processPatientUpdate.getTransactionId());
        }
    },
    ProcessApplicationACK {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessApplicationACK processApplicationACK = VTGWS_ProcessApplicationACK.FromOriginalTransactionIdOk("TEST");
            return new MessageService.MessageResponse(processApplicationACK.toString(), processApplicationACK.getTransactionId());
        }
    },
    ProcessNewOrderRequest {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessNewOrderRequest processNewOrderRequest = VTGWS_ProcessNewOrderRequest.FromMessage(m);
            return new MessageService.MessageResponse(processNewOrderRequest.toString(), processNewOrderRequest.getTransactionId());
        }
    },
    ProcessChangeOrderRequest {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessChangeOrderRequest processChangeOrderRequest = VTGWS_ProcessChangeOrderRequest.FromMessage(m);
            return new MessageService.MessageResponse(processChangeOrderRequest.toString(), processChangeOrderRequest.getTransactionId());
        }
    },
    ProcessCancelOrderRequest {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessCancelOrderRequest processCancelOrderRequest = VTGWS_ProcessCancelOrderRequest.FromMessage(m);
            return new MessageService.MessageResponse(processCancelOrderRequest.toString(), processCancelOrderRequest.getTransactionId());
        }
    },
    ProcessNewOrder {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessNewOrder processNewOrder = VTGWS_ProcessNewOrder.FromMessage(m);
            return new MessageService.MessageResponse(processNewOrder.toString(), processNewOrder.getTransactionId());
        }
    },
    ProcessChangeOrder {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessChangeOrder processChangeOrder = VTGWS_ProcessChangeOrder.FromMessage(m);
            return new MessageService.MessageResponse(processChangeOrder.toString(), processChangeOrder.getTransactionId());
        }
    },
    ProcessCancelOrder {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessCancelOrder processCancelOrder = VTGWS_ProcessCancelOrder.FromMessage(m);
            return new MessageService.MessageResponse(processCancelOrder.toString(), processCancelOrder.getTransactionId());
        }
    },
    ProcessAssignedPathologistUpdate {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessAssignedPathologistUpdate processAssignedPathologistUpdate = VTGWS_ProcessAssignedPathologistUpdate.FromMessage(m);
            return new MessageService.MessageResponse(processAssignedPathologistUpdate.toString(), processAssignedPathologistUpdate.getTransactionId());
        }
    },
    ProcessPhysicianUpdate {
        public MessageService.MessageResponse convert(Message m) {
            VTGWS_ProcessPhysicianUpdate processPhysicianUpdate = VTGWS_ProcessPhysicianUpdate.FromMessage(m);
            return new MessageService.MessageResponse(processPhysicianUpdate.toString(), processPhysicianUpdate.getTransactionId());
        }
    },
    ProcessVANTAGEEvent {
        public MessageService.MessageResponse convert(Message m, Slide slide, String status) {
            VTGWS_ProcessVTGEvent processVTGEvent = VTGWS_ProcessVTGEvent.FromMessage(m, status, slide);
            return new MessageService.MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
        }

        public MessageService.MessageResponse convert(Message m, Block block, String status) {
            VTGWS_ProcessVTGEvent processVTGEvent = VTGWS_ProcessVTGEvent.FromMessage(m, status, block);
            return new MessageService.MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
        }
        public MessageService.MessageResponse convert(Message m, Specimen specimen, String status) {
            VTGWS_ProcessVTGEvent processVTGEvent = VTGWS_ProcessVTGEvent.FromMessage(m, status, specimen);
            return new MessageService.MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
        }
    }
}
