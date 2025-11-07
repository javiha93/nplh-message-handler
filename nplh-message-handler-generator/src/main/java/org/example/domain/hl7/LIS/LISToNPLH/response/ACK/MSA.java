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

    public static MSA CommunicationOK(String originalControlId) {
        MSA msh = new MSA();
        msh.ackCode = "CA";
        msh.messageControlId = originalControlId;

        return msh;
    }

    public static MSA ApplicationOK(String originalControlId) {
        MSA msh = new MSA();
        msh.ackCode = "AA";
        msh.messageControlId = originalControlId;

        return msh;
    }

    public static MSA CommunicationError(String originalControlId, String error) {
        MSA msh = new MSA();
        msh.ackCode = "CE";
        msh.messageControlId = originalControlId;

        return msh;
    }

    public static MSA ApplicationError(String originalControlId, String error) {
        MSA msh = new MSA();
        msh.ackCode = "AE";
        msh.messageControlId = originalControlId;

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
