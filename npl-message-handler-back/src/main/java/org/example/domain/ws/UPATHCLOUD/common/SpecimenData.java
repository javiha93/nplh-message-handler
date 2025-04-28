package org.example.domain.ws.UPATHCLOUD.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.WSSegment;

import java.util.List;

@Data
@NoArgsConstructor
public class SpecimenData extends WSSegment {
    private List<SlideData> allSlideData;
    private String accessionNumber;
    private String specimenNumber;
    private String specimenBarcode;
    private String specimenViewUrl;
    private String status;

    public static SpecimenData FromSpecimen(Message message, Specimen specimen) {
        SpecimenData specimenData = new SpecimenData();

        specimenData.accessionNumber = message.getOrder().getSampleId();
        specimenData.specimenNumber = specimen.getId();
        specimenData.specimenBarcode = specimen.getId();
        specimenData.status = "RELEASED";
        specimenData.specimenViewUrl = "http://hostname/virtuoso/specimenview";



        return specimenData;
    }
}
