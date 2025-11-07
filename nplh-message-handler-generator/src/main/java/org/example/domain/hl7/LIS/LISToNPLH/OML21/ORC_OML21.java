package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.ORC;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class ORC_OML21 extends ORC {

    public static ORC_OML21 Default(String sampleID) {
        return (ORC_OML21) Default(sampleID, new ORC_OML21());
    }

    public static ORC_OML21 FromMessage(Slide slide, Message message) {
        return (ORC_OML21) FromMessage(slide, message, new ORC_OML21());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


