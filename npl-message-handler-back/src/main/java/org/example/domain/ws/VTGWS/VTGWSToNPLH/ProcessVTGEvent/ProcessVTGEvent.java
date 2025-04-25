package org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessVTGEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.VTGWS.WSSegment;

@Data
@NoArgsConstructor
public class ProcessVTGEvent extends WSSegment {

    private String transactionId;
    private EventStatus eventStatus;


    public static ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Specimen specimen) {
        ProcessVTGEvent processVTGEvent = new ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, specimen);

        return processVTGEvent;
    }

    public static ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Block block) {
        ProcessVTGEvent processVTGEvent = new ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, block);

        return processVTGEvent;
    }

    public static ProcessVTGEvent FromMessage(Message message, String eventType, org.example.domain.message.entity.Slide slide) {
        ProcessVTGEvent processVTGEvent = new ProcessVTGEvent();

        processVTGEvent.transactionId = message.getHeader().getMessageControlId();
        processVTGEvent.eventStatus = EventStatus.FromMessage(message, eventType, slide);

        return processVTGEvent;
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
