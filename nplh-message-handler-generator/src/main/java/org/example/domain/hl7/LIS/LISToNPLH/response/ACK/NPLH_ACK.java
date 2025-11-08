package org.example.domain.hl7.LIS.LISToNPLH.response.ACK;

import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.hl7.common.MSH;

import java.util.Objects;

import static org.example.domain.hl7.LIS.LISToNPLH.response.ACK.ERR.parseERR;
import static org.example.domain.hl7.LIS.LISToNPLH.response.ACK.MSA.parseMSA;
import static org.example.domain.hl7.LIS.LISToNPLH.response.ACK.MSH_ACK.parseMSH;

public class NPLH_ACK extends HL7Segment implements HL7Message {
    MSH_ACK msh;
    MSA msa;
    ERR err;

    public static NPLH_ACK CommunicationOK(String originalControlId){
        NPLH_ACK ack = new NPLH_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.CommunicationOK(originalControlId);

        return ack;
    }

    public static NPLH_ACK CommunicationOK(String originalControlId, String controlId){
        NPLH_ACK ack = new NPLH_ACK();

        ack.msh = MSH_ACK.Default(controlId);
        ack.msa = MSA.CommunicationOK(originalControlId);

        return ack;
    }

    public static NPLH_ACK CommunicationError(String originalControlId, String error){
        NPLH_ACK ack = new NPLH_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.CommunicationError(originalControlId, error);

        return ack;
    }

    public static NPLH_ACK ApplicationOK(String originalControlId){
        NPLH_ACK ack = new NPLH_ACK();

        ack.msh = MSH_ACK.Default();
        ack.msa = MSA.ApplicationOK(originalControlId);

        return ack;
    }

    public static NPLH_ACK ApplicationOK(String originalControlId, String controlId){
        NPLH_ACK ack = new NPLH_ACK();

        ack.msh = MSH_ACK.Default(controlId);
        ack.msa = MSA.ApplicationOK(originalControlId);

        return ack;
    }

    public static NPLH_ACK ApplicationError(String originalControlId, String error){
        NPLH_ACK ack = new NPLH_ACK();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NPLH_ACK ack = (NPLH_ACK) o;
        return Objects.equals(msh, ack.msh) &&
                Objects.equals(msa, ack.msa) &&
                Objects.equals(err, ack.err);
    }

    public static NPLH_ACK fromString(String hl7Message) {
        if (hl7Message == null || hl7Message.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty message");
        }

        NPLH_ACK ack = new NPLH_ACK();

        String[] lines = hl7Message.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            String segmentType = line.substring(0, 3);

            switch (segmentType) {
                case "MSH":
                    ack.msh = parseMSH(line);
                    break;
                case "MSA":
                    ack.msa = parseMSA(line);
                    break;
                case "ERR":
                    ack.err = parseERR(line);
                    break;
            }
        }

        return ack;
    }
}
