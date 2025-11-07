package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;

@Data
@NoArgsConstructor
public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID) {
        return (ORC) org.example.domain.hl7.common.ORC.Default(sampleID, new ORC());
    }

    public static ORC FromMessage(Slide slide, Message message) {
        return (ORC) org.example.domain.hl7.common.ORC.FromMessage(slide, message, new ORC());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


