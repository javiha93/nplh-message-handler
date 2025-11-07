package org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendReleasedSpecimen;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.UPATHCLOUD.common.SpecimenData;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class UPATHCLOUD_SendReleasedSpecimen extends WSSegment {
    private SpecimenData specimenData;

    public static UPATHCLOUD_SendReleasedSpecimen FromSpecimen(Message message, Specimen specimen) {
        UPATHCLOUD_SendReleasedSpecimen sendReleasedSpecimen = new UPATHCLOUD_SendReleasedSpecimen();

        sendReleasedSpecimen.specimenData = SpecimenData.FromSpecimen(message, specimen);

        return sendReleasedSpecimen;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<sendReleasedSpecimen>\n" +
                addIndentation(indentationLevel) + "<arg0>\n" + specimenData.toString(indentationLevel + 1) + addIndentation(indentationLevel) + "</arg0>\n" +
                "</sendReleasedSpecimen>";
    }
}
