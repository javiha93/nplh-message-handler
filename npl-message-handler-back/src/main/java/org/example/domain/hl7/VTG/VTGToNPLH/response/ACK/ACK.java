package org.example.domain.hl7.VTG.VTGToNPLH.response.ACK;

import org.example.domain.hl7.HL7Segment;

public class ACK extends HL7Segment {
    MSH msh;
    MSA msa;
    ERR err;

    public static ACK CommunicationOK(String originalControlId){
        ACK ack = new ACK();

        ack.msh = MSH.Default();
        ack.msa = MSA.CommunicationOK(originalControlId);

        return ack;
    }

    public static ACK CommunicationError(String originalControlId, String error){
        ACK ack = new ACK();

        ack.msh = MSH.Default();
        ack.msa = MSA.CommunicationError(originalControlId, error);
        ack.err = ERR.FromError(error);

        return ack;
    }

    public static ACK ApplicationOK(String originalControlId){
        ACK ack = new ACK();

        ack.msh = MSH.Default();
        ack.msa = MSA.ApplicationOK(originalControlId);

        return ack;
    }

    public static ACK ApplicationError(String originalControlId, String error){
        ACK ack = new ACK();

        ack.msh = MSH.Default();
        ack.msa = MSA.ApplicationError(originalControlId, error);
        ack.err = ERR.FromError(error);

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
