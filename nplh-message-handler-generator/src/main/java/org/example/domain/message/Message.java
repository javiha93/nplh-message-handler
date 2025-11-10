package org.example.domain.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.example.domain.message.entity.*;
import org.example.domain.message.professional.Physician;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Message extends Reflection implements Cloneable {

    String channelType;
    LocalDateTime registerTime;
    MessageHeader header;
    Patient patient;
    Physician physician;
    String actionCode;
    String error;

    public Message() {
        this.header = new MessageHeader();
        this.patient = new Patient();
        this.physician = new Physician();
    }

    public static Message Default(String messageType, String sampleId) {
        Message message = new Message();

        message.setHeader(MessageHeader.Default(messageType));
        message.setPatient(Patient.Default(sampleId));
        message.setPhysician(Physician.Default());

        return message;
    }

    public static Message Default(String sampleId) {
        Message message = new Message();

        message.setHeader(MessageHeader.Default());
        message.setPatient(Patient.Default(sampleId));
        message.setPhysician(Physician.Default());

        return message;
    }

    public void setRegisterTime(String registerTime) {
        if ((registerTime == null) || (registerTime.isEmpty())){
            return;
        }
        DateTimeFormatter formatter;
        if (registerTime.getBytes().length == 14) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        } else if (registerTime.getBytes().length == 8) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        } else {
            return;
        }
        this.registerTime = LocalDateTime.parse(registerTime, formatter);
    }

    public String getRegisterTime() {
        if (registerTime == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return registerTime.format(formatter);
    }

    public void setStainProtocol(StainProtocol stainProtocol) {
        for (Slide slide : getAllSlides()) {
            slide.setStainProtocol(stainProtocol);
        }
    }

    public List<Slide> getAllSlides() {
        return patient.getAllSlides();
    }

    public Slide getSingleSlide() {
        return getPatient().getSingleSlide();
    }

    public Slide getSlide(String id) {
        return getPatient().getSlide(id);
    }

    public List<Block> getAllBlocks() {
        return patient.getAllBlocks();
    }
    public Block getBlock() {
        return patient.getBlock();
    }

    public List<Specimen> getAllSpecimens() {
        return patient.getAllSpecimens();
    }

    public List<Order> getAllOrders() {
        return patient.getAllOrders();
    }

    public Specimen getSingleSpecimen() {return patient.getSingleSpecimen();}

    public Specimen getFirstSpecimen() {return patient.getFirstSpecimen();
    }
    public Specimen getSpecimen(String id) {return patient.getSpecimen(id);}

    public Order getOrder() { return patient.getSingleOrder(); }
    public Order getOrder(String caseId) { return patient.getSingleOrder(caseId); }

    @Override
    public Message clone() {
        try {
            Message cloned = (Message) super.clone();
            if (this.header != null) {
                cloned.setHeader(this.header.clone());
            } else {
                cloned.setHeader(null);
            }
            if (this.patient != null) {
                cloned.setPatient(this.patient.clone());
            } else {
                cloned.setPatient(null);
            }
            if (this.physician != null) {
                cloned.setPhysician(this.physician.clone());
            } else {
                cloned.setPhysician(null);
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Specimen", e);
        }
    }

}
