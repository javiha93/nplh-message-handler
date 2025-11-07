package org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendSlideWSAData;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;
import org.example.domain.message.Message;

@Data
@NoArgsConstructor
public class VTGWS_SendSlideWSAData extends WSSegment {
    private String accessionNumber;
    private String specimenNumber;
    private String blockNumber;
    private String slideNumber;
    private String slideLabelId;
    private String wsaStatus;

    public static VTGWS_SendSlideWSAData FromMessage(Message message, Slide slide, String status) {
        VTGWS_SendSlideWSAData sendSlideWSAData = new VTGWS_SendSlideWSAData();

        sendSlideWSAData.setAccessionNumber(message.getOrder().getSampleId());
        sendSlideWSAData.setSlideLabelId(slide.getId());
        sendSlideWSAData.setWsaStatus(status);

        return sendSlideWSAData;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<sendSlideWSAData>\n" +
                addIndentation(indentationLevel) + "<accessionNumber>" + accessionNumber + "</accessionNumber>\n" +
                addIndentation(indentationLevel) + "<specimenNumber>" + nullSafe(specimenNumber) + "</specimenNumber>\n" +
                addIndentation(indentationLevel) + "<blockNumber>" + nullSafe(blockNumber) + "</blockNumber>\n" +
                addIndentation(indentationLevel) + "<slideNumber>" + nullSafe(slideNumber) + "</slideNumber>\n" +
                addIndentation(indentationLevel) + "<slideLabelId>" + slideLabelId + "</slideLabelId>\n" +
                addIndentation(indentationLevel) + "<wsaStatus>" + wsaStatus + "</wsaStatus>\n" +
                "</sendSlideWSAData>";
    }
}
