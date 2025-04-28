package org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendReleasedSpecimen;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.UPATHCLOUD.common.SpecimenData;
import org.example.domain.ws.WSSegment;

@Data
@NoArgsConstructor
public class SendReleasedSpecimen extends WSSegment {
    private SpecimenData specimenData;


    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<sendReleasedSpecimen>\n" +
                addIndentation(indentationLevel) + "<arg0>" + specimenData + "</arg0>\n" +
                "</sendReleasedSpecimen>";
    }
}
