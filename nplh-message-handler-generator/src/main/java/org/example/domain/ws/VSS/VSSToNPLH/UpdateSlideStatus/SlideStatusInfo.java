package org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.StainingInfo;
import org.example.domain.ws.VSS.common.Observation;
import org.example.domain.ws.VSS.common.Reagent;
import org.example.domain.ws.VSS.common.Slide;
import org.example.domain.ws.VSS.common.StainProtocol;
import org.example.domain.ws.WSSegment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SlideStatusInfo extends WSSegment {
    private String slideStatus;
    private String orderStatus;
    private Slide slide;
    private StainProtocol stainProtocol;
    private String keyValueType;
    private String key;
    private String value;
    private List<Reagent> reagents = new ArrayList<>();
    private List<Observation> observations = new ArrayList<>();


    public static SlideStatusInfo FromSlide(Message message, org.example.domain.message.entity.Slide slide, String slideStatus) {
        SlideStatusInfo slideStatusInfo = new SlideStatusInfo();

        slideStatusInfo.slideStatus = slideStatus;
        slideStatusInfo.orderStatus = "CM";
        slideStatusInfo.slide = Slide.FromSlide(slide);

        if (slide.getReagents() != null) {
            for (org.example.domain.message.entity.Reagent entityReagent : slide.getReagents()) {
                slideStatusInfo.reagents.add(Reagent.FromReagent(entityReagent));
            }
        }

        slide.setStainingInfo(StainingInfo.fullStainingInfo());

        StainingInfo stainingInfo = slide.getStainingInfo();
        for (Field field : stainingInfo.getClass().getDeclaredFields()) {
            if (field.trySetAccessible()) {
                try {
                    String fieldName = field.getName();
                    Object fieldValue = field.get(stainingInfo);

                    if (fieldValue != null) {
                        Observation observation = Observation.fromStainingInfo(fieldName, (String) fieldValue, slide.getStainProtocol());
                        slideStatusInfo.observations.add(observation);
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
//
//        if (slide.getStainProtocol() != null) {
//            slideStatusInfo.setKeyValueType("DT");
//            slideStatusInfo.stainProtocol = StainProtocol.FromStainProtocol(slide.getStainProtocol());
//            slideStatusInfo.key = "StainingHostID";
//            slideStatusInfo.value = "123";
//        }

        return slideStatusInfo;
    }

    public String toString(int indentationLevel) {
        String slideStatusInfo = "<SlideStatusInfo>\n"
                + addIndentation(indentationLevel) + "<SlideStatus>" + nullSafe(slideStatus) + "</SlideStatus>\n"
                + addIndentation(indentationLevel) + "<OrderStatus>" + nullSafe(orderStatus) + "</OrderStatus>\n"
                + nullSafe(slide, Slide::new).toString(indentationLevel) + "\n";

        slideStatusInfo += addIndentation(indentationLevel) + "<Observations>\n";
        for (Observation observation : observations) {
            slideStatusInfo += nullSafe(observation, Observation::new).toString(indentationLevel + 1) + "\n";
        }
        slideStatusInfo += addIndentation(indentationLevel) + "</Observations>\n";

        slideStatusInfo += addIndentation(indentationLevel) + "<ReagentInfo>\n";
        for (Reagent reagent : reagents) {
            slideStatusInfo += nullSafe(reagent, Reagent::new).toString(indentationLevel + 1) + "\n";
        }
        slideStatusInfo += addIndentation(indentationLevel) + "</ReagentInfo>\n";

        slideStatusInfo += addIndentation(indentationLevel - 1) + "</SlideStatusInfo>";
        return slideStatusInfo;
    }
}
