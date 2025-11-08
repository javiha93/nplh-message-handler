package org.example.domain.hl7.LIS.NPLHToLIS.response.ACK;

import org.example.domain.hl7.HL7Segment;

public class LIS_ACK extends HL7Segment {
    MSH_ACK msh;
    MSA msa;
    ERR err;

    public static LIS_ACK CommunicationOK(String originalControlId){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.CommunicationOK(originalControlId);

        return ack;
    }

    public static LIS_ACK CommunicationOK(String originalControlId, String controlId){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default(controlId);
        ack.msa = MSA.CommunicationOK(originalControlId);

        return ack;
    }

    public static LIS_ACK CommunicationError(String originalControlId, String error){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.CommunicationError(originalControlId, error);

        return ack;
    }

    public static LIS_ACK ApplicationOK(String originalControlId){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.ApplicationOK(originalControlId);

        return ack;
    }

    public static LIS_ACK ApplicationOK(String originalControlId, String controlId){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default(controlId);
        ack.msa = MSA.ApplicationOK(originalControlId);

        return ack;
    }

    public static LIS_ACK ApplicationError(String originalControlId, String error){
        LIS_ACK ack = new LIS_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.ApplicationError(originalControlId, error);

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
