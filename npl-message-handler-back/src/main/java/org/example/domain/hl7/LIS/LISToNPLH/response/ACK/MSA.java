package org.example.domain.hl7.LIS.LISToNPLH.response.ACK;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

import java.util.UUID;

public class MSA extends HL7Segment {
    @HL7Position(position = 1)
    private String ackCode;

    @HL7Position(position = 4)
    private String messageControlId;

    public static MSA Default() {
        MSA msh = new MSA();
        msh.ackCode = "CA";
        msh.messageControlId = UUID.randomUUID().toString();

        return msh;
    }

    @Override
    public String toString() {
        String value = "MSA|" +
                nullSafe(ackCode) + "|" +                            // 1
                nullSafe(messageControlId) + "|";                    // 2
        return cleanSegment(value);
    }

}
