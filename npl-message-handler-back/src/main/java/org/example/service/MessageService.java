
package org.example.service;

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

    public String convertMessage(Message message, String messageType) {
        switch (messageType) {
            case "OML21":
                OML21 oml21 = OML21.FromMessage(message);
                return oml21.toString();
            case "DELETE_SLIDE":
                DELETESLIDE deleteslide = DELETESLIDE.FromMessage(message, new Slide());
                return deleteslide.toString();
            case "ADTA28":
                ADTA28 adta28 = ADTA28.FromMessage(message);
                return adta28.toString();
            case "ADTA08":
                ADTA08 adta08 = ADTA08.FromMessage(message);
                return adta08.toString();
            case "DELETE_CASE":
                DELETECASE deletecase = DELETECASE.FromMessage(message);
                return deletecase.toString();
            case "OEWF":
                OEWF oewf = OEWF.FromMessage(message);
                return oewf.toString();
            case "ADDITION":
                Addition addition = Addition.FromMessage(message);
                return addition.toString();
            case "ProcessPatientUpdate":
                ProcessPatientUpdate processPatientUpdate = ProcessPatientUpdate.FromMessage(message);
                return processPatientUpdate.toString();
            case "ProcessNewOrderRequest":
                ProcessNewOrderRequest processNewOrderRequest = ProcessNewOrderRequest.FromMessage(message);
                return processNewOrderRequest.toString();
            case "ProcessChangeOrderRequest":
                ProcessChangeOrderRequest processChangeOrderRequest = ProcessChangeOrderRequest.FromMessage(message);
                return processChangeOrderRequest.toString();
            case "ProcessCancelOrderRequest":
                ProcessCancelOrderRequest processCancelOrderRequest = ProcessCancelOrderRequest.FromMessage(message);
                return processCancelOrderRequest.toString();
            case "ProcessNewOrder":
                ProcessNewOrder processNewOrder = ProcessNewOrder.FromMessage(message);
                return processNewOrder.toString();
            case "ProcessChangeOrder":
                ProcessChangeOrder processChangeOrder = ProcessChangeOrder.FromMessage(message);
                return processChangeOrder.toString();
            case "ProcessCancelOrder":
                ProcessCancelOrder processCancelOrder = ProcessCancelOrder.FromMessage(message);
                return processCancelOrder.toString();
            case "ProcessAssignedPathologistUpdate":
                ProcessAssignedPathologistUpdate processAssignedPathologistUpdate = ProcessAssignedPathologistUpdate.FromMessage(message);
                return processAssignedPathologistUpdate.toString();
            case "ProcessPhysicianUpdate":
                ProcessPhysicianUpdate processPhysicianUpdate = ProcessPhysicianUpdate.FromMessage(message);
                return processPhysicianUpdate.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, Specimen specimen) {
        switch (messageType) {
            case "DELETE_SPECIMEN":
                DELETESPECIMEN deletespecimen = DELETESPECIMEN.FromMessage(message, specimen);
                return deletespecimen.toString();
            case "sendReleasedSpecimen":
                SendReleasedSpecimen sendReleasedSpecimen = SendReleasedSpecimen.FromSpecimen(message, specimen);
                return sendReleasedSpecimen.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, Slide slide) {
        switch (messageType) {
            case "DELETE_SLIDE":
                DELETESLIDE deleteSlide = DELETESLIDE.FromMessage(message, slide);
                return deleteSlide.toString();
            case "sendScannedSlideImageLabelId":
                SendScannedSlide sendScannedSlide = SendScannedSlide.FromMessage(slide);

                //WSClient wsClient = new WSClient(WSHost.UPATH_CLOUD);
                //wsClient.sendSoapMessage(messageType, sendScannedSlide.toString());
                return sendScannedSlide.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, Slide slide, String status) {
        switch (messageType) {
            case "SLIDE_UPDATE":
                SlideUpdate slideUpdate = SlideUpdate.FromMessage(message, slide, status);
                return slideUpdate.toString();
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, slide);
                return processVTGEvent.toString();
            case "sendSlideWSAData":
                SendSlideWSAData sendSlideWSAData = SendSlideWSAData.FromMessage(message, slide, status);
                return sendSlideWSAData.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, Block block, String status) {
        switch (messageType) {
            case "BLOCK_UPDATE":
                BlockUpdate blockUpdate = BlockUpdate.FromMessage(message, block, status);
                return blockUpdate.toString();
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, block);
                return processVTGEvent.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, Specimen specimen, String status) {
        switch (messageType) {
            case "SPECIMEN_UPDATE":
                SpecimenUpdate slideUpdate = SpecimenUpdate.FromMessage(message, specimen, status);
                return slideUpdate.toString();
            case "ProcessVANTAGEEvent":
                ProcessVTGEvent processVTGEvent = ProcessVTGEvent.FromMessage(message, status, specimen);
                return processVTGEvent.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }

    public String convertMessage(Message message, String messageType, String status) {
        switch (messageType) {
            case "CASE_UPDATE":
                CASEUPDATE caseupdate = CASEUPDATE.FromMessage(message, status);
                return caseupdate.toString();
            default:
                throw new IllegalArgumentException("Tipo de mensaje no soportado: " + messageType);
        }
    }
}
