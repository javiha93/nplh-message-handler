package org.example.domain.hl7.LIS.LISToNPLH.response.ACK;

import org.example.domain.hl7.HL7Segment;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.dto.OSegment;

public class ACK extends HL7Segment {
    MSH msh;
    MSA msa;
    ERR err;

    public static ACK ProcessedOK(){
        ACK ack = new ACK();

        ack.msh = MSH.Default();
        ack.msa = MSA.Default();

        return ack;
    }

    @Override
    public String toString() {
        String oml21 = nullSafe(msh) + "\n" +
                nullSafe(msa) + "\n" +
                nullSafe(err) + "\n";
        return cleanMessage(oml21);
    }
}
