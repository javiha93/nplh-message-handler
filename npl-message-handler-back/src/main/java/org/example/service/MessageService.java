
package org.example.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.*;
import org.example.domain.hl7.LIS.LISToNPLH.ADTA08.ADTA08;
import org.example.domain.hl7.LIS.LISToNPLH.ADTA28.ADTA28;
import org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE.CASEUPDATE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETECASE.DELETECASE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE.DELETESLIDE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESPECIMEN.DELETESPECIMEN;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.dto.OML21;
import org.example.domain.hl7.VTG.VTGToNPLH.ADDITION.Addition;
import org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE.BlockUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.OEWF.OEWF;
import org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE.SlideUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.SPECIMENUPDATE.SpecimenUpdate;
import org.example.domain.host.Host;
import org.example.domain.host.HostDeserializer;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendReleasedSpecimen.SendReleasedSpecimen;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendScannedSlide.SendScannedSlide;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendSlideWSAData.SendSlideWSAData;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessAssignedPathologistUpdate.ProcessAssignedPathologistUpdate;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrder.ProcessCancelOrder;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessCancelOrderRequest.ProcessCancelOrderRequest;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrder.ProcessChangeOrder;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessChangeOrderRequest.ProcessChangeOrderRequest;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrder.ProcessNewOrder;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrderRequest.ProcessNewOrderRequest;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPatientUpdate.ProcessPatientUpdate;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessPhysicianUpdate.ProcessPhysicianUpdate;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessVTGEvent.ProcessVTGEvent;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public Message generateMessage(String sampleId) {
        return Message.Default(sampleId);
    }

    public Message generateMessage() {
        return Message.Default("");
    }

    public MessageResponse convertMessage(Message message, String messageType) {
        switch (messageType) {
            case "OML21":
                OML21 oml21 = OML21.FromMessage(message);
                return new MessageResponse(oml21.toString(), oml21.getControlId());
            case "DELETE_SLIDE":
                DELETESLIDE deleteslide = DELETESLIDE.FromMessage(message, new Slide());
                return new MessageResponse(deleteslide.toString(), deleteslide.getControlId());
            case "ADTA28":
                ADTA28 adta28 = ADTA28.FromMessage(message);
                return new MessageResponse(adta28.toString(), adta28.getControlId());
            case "ADTA08":
                ADTA08 adta08 = ADTA08.FromMessage(message);
                return new MessageResponse(adta08.toString(), adta08.getControlId());
            case "DELETE_CASE":
                DELETECASE deletecase = DELETECASE.FromMessage(message);
                return new MessageResponse(deletecase.toString(), deletecase.getControlId());
            case "OEWF":
                OEWF oewf = OEWF.FromMessage(message);
                return new MessageResponse(oewf.toString(), oewf.getControlId());
            case "ADDITION":
                Addition addition = Addition.FromMessage(message);
                return new MessageResponse(addition.toString(), addition.getControlId());
            case "ProcessPatientUpdate":
                ProcessPatientUpdate processPatientUpdate = ProcessPatientUpdate.FromMessage(message);
                return new MessageResponse(processPatientUpdate.toString(), processPatientUpdate.getTransactionId());
            case "ProcessNewOrderRequest":
                ProcessNewOrderRequest processNewOrderRequest = ProcessNewOrderRequest.FromMessage(message);
                return new MessageResponse(processNewOrderRequest.toString(), processNewOrderRequest.getTransactionId());
            case "ProcessChangeOrderRequest":
                ProcessChangeOrderRequest processChangeOrderRequest = ProcessChangeOrderRequest.FromMessage(message);
                return new MessageResponse(processChangeOrderRequest.toString(), processChangeOrderRequest.getTransactionId());
            case "ProcessCancelOrderRequest":
                ProcessCancelOrderRequest processCancelOrderRequest = ProcessCancelOrderRequest.FromMessage(message);
                return new MessageResponse(processCancelOrderRequest.toString(), processCancelOrderRequest.getTransactionId());
            case "ProcessNewOrder":
                ProcessNewOrder processNewOrder = ProcessNewOrder.FromMessage(message);
                return new MessageResponse(processNewOrder.toString(), processNewOrder.getTransactionId());
            case "ProcessChangeOrder":
                ProcessChangeOrder processChangeOrder = ProcessChangeOrder.FromMessage(message);
                return new MessageResponse(processChangeOrder.toString(), processChangeOrder.getTransactionId());
            case "ProcessCancelOrder":
                ProcessCancelOrder processCancelOrder = ProcessCancelOrder.FromMessage(message);
                return new MessageResponse(processCancelOrder.toString(), processCancelOrder.getTransactionId());
            case "ProcessAssignedPathologistUpdate":
                ProcessAssignedPathologistUpdate processAssignedPathologistUpdate = ProcessAssignedPathologistUpdate.FromMessage(message);
                return new MessageResponse(processAssignedPathologistUpdate.toString(), processAssignedPathologistUpdate.getTransactionId());
            case "ProcessPhysicianUpdate":
                ProcessPhysicianUpdate processPhysicianUpdate = ProcessPhysicianUpdate.FromMessage(message);
                return new MessageResponse(processPhysicianUpdate.toString(), processPhysicianUpdate.getTransactionId());
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, Specimen specimen) {
        switch (messageType) {
            case "DELETE_SPECIMEN":
                DELETESPECIMEN deletespecimen = DELETESPECIMEN.FromMessage(message, specimen);
                return new MessageResponse(deletespecimen.toString(), deletespecimen.getControlId());
            case "sendReleasedSpecimen":
                SendReleasedSpecimen sendReleasedSpecimen = SendReleasedSpecimen.FromSpecimen(message, specimen);
                return new MessageResponse(sendReleasedSpecimen.toString(), "");
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, Slide slide) {
        switch (messageType) {
            case "DELETE_SLIDE":
                DELETESLIDE deleteSlide = DELETESLIDE.FromMessage(message, slide);
                return new MessageResponse(deleteSlide.toString(), deleteSlide.getControlId());
            case "sendScannedSlideImageLabelId":
                SendScannedSlide sendScannedSlide = SendScannedSlide.FromMessage(slide);
                return new MessageResponse(sendScannedSlide.toString(), "");
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, Slide slide, String status) {
        switch (messageType) {
            case "SLIDE_UPDATE":
                SlideUpdate slideUpdate = SlideUpdate.FromMessage(message, slide, status);
                return new MessageResponse(slideUpdate.toString(), slideUpdate.getControlId());
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, slide);
                return new MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
            case "sendSlideWSAData":
                SendSlideWSAData sendSlideWSAData = SendSlideWSAData.FromMessage(message, slide, status);
                return new MessageResponse(sendSlideWSAData.toString(), "");
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, Block block, String status) {
        switch (messageType) {
            case "BLOCK_UPDATE":
                BlockUpdate blockUpdate = BlockUpdate.FromMessage(message, block, status);
                return new MessageResponse(blockUpdate.toString(), blockUpdate.getControlId());
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, block);
                return new MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, Specimen specimen, String status) {
        switch (messageType) {
            case "SPECIMEN_UPDATE":
                SpecimenUpdate specimenUpdate = SpecimenUpdate.FromMessage(message, specimen, status);
                return new MessageResponse(specimenUpdate.toString(), specimenUpdate.getControlId());
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, specimen);
                return new MessageResponse(processVTGEvent.toString(), processVTGEvent.getTransactionId());
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public MessageResponse convertMessage(Message message, String messageType, String status) {
        switch (messageType) {
            case "CASE_UPDATE":
                CASEUPDATE caseupdate = CASEUPDATE.FromMessage(message, status);
                return new MessageResponse(caseupdate.toString(), caseupdate.getControlId());
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class MessageResponse {
        private String message;
        private String controlId;
    }
}
