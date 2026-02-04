package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessVTGEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class VTGWS_ProcessVTGEvent extends WSSegment implements WSMessage {

    private String transactionId;
    private EventStatus eventStatus;


    public static VTGWS_ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Specimen specimen) {
        VTGWS_ProcessVTGEvent processVTGEvent = new VTGWS_ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, specimen);

        return processVTGEvent;
    }

    public static VTGWS_ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Block block) {
        VTGWS_ProcessVTGEvent processVTGEvent = new VTGWS_ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, block);

        return processVTGEvent;
    }

    public static VTGWS_ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Slide slide) {
        VTGWS_ProcessVTGEvent processVTGEvent = new VTGWS_ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, slide);

        return processVTGEvent;
    }

    @Override
    public String getSoapAction() {
        return "ProcessVANTAGEEvent";
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<ProcessVTGEvent>\n" +
                addIndentation(indentationLevel) + "<transactionId>" + transactionId + "</transactionId>\n" +
                addIndentation(indentationLevel) + eventStatus.toString(indentationLevel) + "\n" +
                "</ProcessVTGEvent>";
    }
}
