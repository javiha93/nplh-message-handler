package org.example.domain.hl7.LIS.LISToNPLH.MFQM1;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MSH extends org.example.domain.hl7.common.MSH {
    public static MSH Default()
    {
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.Default();
        msh.setMessageType("MFQ");
        msh.setMessageEvent("M01");

        return msh;
    }
}
