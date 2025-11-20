package org.example.domain.hl7.VTG.VTGToNPLH.response.ACK;

import lombok.Data;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

import java.util.Objects;

@Data
public class MSA extends HL7Segment {
    @HL7Position(position = 1)
    private String ackCode;

    @HL7Position(position = 4)
    private String messageControlId;

    public static MSA CommunicationOK(String originalControlId) {
        MSA msa = new MSA();
        msa.ackCode = "CA";
        msa.messageControlId = originalControlId;

        return msa;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSA msa = (MSA) o;
        return Objects.equals(ackCode, msa.ackCode);
    }

    protected static MSA parseMSA(String line) {
        MSA msa = new MSA();
        String[] fields = line.split("\\|");

        if (fields.length > 1) msa.setAckCode(getFieldValue(fields, 1));
        if (fields.length > 2) msa.setMessageControlId(getFieldValue(fields, 2));

        return msa;
    }
}
