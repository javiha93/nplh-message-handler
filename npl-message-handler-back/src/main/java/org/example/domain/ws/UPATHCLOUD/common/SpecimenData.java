package org.example.domain.ws.UPATHCLOUD.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.ws.VTGWS.common.StainOrder;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


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
        List<Slide> slideList = specimen.getAllSlides();

        specimenData.accessionNumber = message.getOrder().getSampleId();
        specimenData.specimenNumber = specimen.getId();
        specimenData.specimenBarcode = specimen.getId();
        specimenData.status = "RELEASED";
        specimenData.specimenViewUrl = "http://hostname/virtuoso/specimenview";

        List<SlideData> slideDataList = new ArrayList<>();
        for (Slide slide : slideList) {
            slideDataList.add(SlideData.FromSlide(slide));
        }
        specimenData.allSlideData = slideDataList;

        return specimenData;
    }

    public boolean isEmpty() {
        return Stream.of(accessionNumber, specimenNumber, specimenBarcode)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }

    public String toString(int indentationLevel) {
        String specimenData = addIndentation(indentationLevel) + "<specimenData>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            for (SlideData slideData : allSlideData) {
                specimenData += nullSafe(slideData, SlideData::new).toString(indentationLevel) + "\n";
            }

            specimenData += addIndentation(indentationLevel) + "<accessionNumber>" +  nullSafe(accessionNumber) + "</accessionNumber>\n"
                    + addIndentation(indentationLevel) + "<specimenNumber>" +  nullSafe(specimenNumber) + "</specimenNumber>\n"
                    + addIndentation(indentationLevel) + "<specimenBarcode>" +  nullSafe(specimenBarcode) + "</specimenBarcode>\n"
                    + addIndentation(indentationLevel) + "<specimenViewUrl>" +  nullSafe(specimenViewUrl) + "</specimenViewUrl>\n"
                    + addIndentation(indentationLevel) + "<status>" +  nullSafe(status) + "</status>\n";

            indentationLevel --;
        }

        specimenData += addIndentation(indentationLevel) + "</specimenData>\n";
        return specimenData;
    }
}
