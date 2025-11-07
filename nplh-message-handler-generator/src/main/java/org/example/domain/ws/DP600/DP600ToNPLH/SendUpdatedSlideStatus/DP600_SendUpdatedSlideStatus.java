package org.example.domain.ws.DP600.DP600ToNPLH.SendUpdatedSlideStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class DP600_SendUpdatedSlideStatus extends WSSegment {
    private String slideId;
    private String status;
    private String description;
    private String scannerSerial;
    private String location;
    private String pacsAet;
    private String studyInstanceUID;
    private String SOPInstanceUID;

    public static DP600_SendUpdatedSlideStatus FromMessage(Slide slide, String status) {
        DP600_SendUpdatedSlideStatus sendUpdatedSlideStatus = new DP600_SendUpdatedSlideStatus();

        sendUpdatedSlideStatus.setSlideId(slide.getId());
        sendUpdatedSlideStatus.setStatus(status);
        sendUpdatedSlideStatus.setDescription("sNDecription");
        sendUpdatedSlideStatus.setScannerSerial("SS");
        sendUpdatedSlideStatus.setLocation("location");
        sendUpdatedSlideStatus.setPacsAet("studyInstanceUID");
        sendUpdatedSlideStatus.setStudyInstanceUID("2.16.840.1.113995.3.110.3.0.10118.6000009.956105.542979");
        sendUpdatedSlideStatus.setSOPInstanceUID("2.16.840.1.113995.3.110.3.0.10118.6000009.843484");

        return sendUpdatedSlideStatus;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<sendUpdatedSlideStatus>\n" +
                addIndentation(indentationLevel) + "<arg0>" + slideId + "</arg0>\n" +
                addIndentation(indentationLevel) + "<arg1>" + status + "</arg1>\n" +
                addIndentation(indentationLevel + 1) + "<slideStatusData>\n" +
                addIndentation(indentationLevel + 2) + " <description>" + description + "</description>\n" +
                addIndentation(indentationLevel + 2) + " <scannerSerial>" + scannerSerial + "</scannerSerial>\n" +
                addIndentation(indentationLevel + 2) + " <location>" + location + "</location>\n" +
                addIndentation(indentationLevel + 2) + " <pacsAet>" + pacsAet + "</pacsAet>\n" +
                addIndentation(indentationLevel + 2) + " <studyInstanceUID>" + studyInstanceUID + "</studyInstanceUID>\n" +
                addIndentation(indentationLevel + 2) + " <SOPInstanceUID>" + SOPInstanceUID + "</SOPInstanceUID>\n" +
                addIndentation(indentationLevel + 1) + "</slideStatusData>\n" +
                "</sendUpdatedSlideStatus>";
    }
}
