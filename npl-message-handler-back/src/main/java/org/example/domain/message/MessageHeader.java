package org.example.domain.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
public class MessageHeader extends Reflection implements Cloneable {

    private String messageType;
    private String messageEvent;
    //    private String actionCode;
//    private String systemName;
    private String sendingApplication;
    private String sendingFacility;
    private String receivingApplication;
    private String receivingFacility;
    private LocalDateTime messageDateTime;
    private String messageControlId;
    private String originalMessageControlId;
    //    private String acknowledgmentCode;
//    private String oriMessageControlID;
//    private String errorCondition;
//    private String webServiceMethod;
    private String processingId;
    private String versionId;
    //    private String textMessage;
//    private String headerOPT1;
//    private String headerOPT2;
//    private String headerOPT3;
//    private String headerOPT4;
//    private String headerOPT5;
    private String workflowId;

    public static MessageHeader Default(String messageType)
    {
        MessageHeader messageHeader = Default();

        String[] messageTypeParts = messageType.split("\\^");
        messageHeader.setMessageType(messageTypeParts[0]);
        messageHeader.setMessageEvent(messageTypeParts[1]);

        return messageHeader;
    }

    public static MessageHeader Default()
    {
        MessageHeader messageHeader = new MessageHeader();

        messageHeader.setSendingApplication("LIS");
        messageHeader.setSendingFacility("XYZ Laboratory");
        messageHeader.setReceivingApplication("Ventana");
        messageHeader.setReceivingFacility("ABC Laboratory");
        messageHeader.setMessageDateTime("20240518170503");
        messageHeader.setMessageControlId(UUID.randomUUID().toString());
        messageHeader.setProcessingId("P");
        messageHeader.setVersionId("2.4");

        return messageHeader;
    }

    public void setMessageDateTime(String messageDateTime) {
        if ((messageDateTime == null) || (messageDateTime.isEmpty())){
            return;
        }
        DateTimeFormatter formatter;
        if (messageDateTime.getBytes().length == 14) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        } else if (messageDateTime.getBytes().length == 8) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        } else {
            return;
        }
        this.messageDateTime = LocalDateTime.parse(messageDateTime, formatter);
    }

    public String getMessageDateTime() {
        if (messageDateTime == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTime = messageDateTime.format(formatter);
        if (dateTime.endsWith("000000")) {
            return dateTime.substring(0, 8);
        }
        return dateTime;
    }

    public void setMessageControlId(String value){
        this.messageControlId = value;
        this.originalMessageControlId = value;
    }

    public String getMessageControlId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public MessageHeader clone() {
        try {
            return  (MessageHeader) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for MessageHeader", e);
        }
    }
}
