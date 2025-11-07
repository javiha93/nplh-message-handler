package org.example.domain.hl7.LIS.LISToNPLH.response.ACK;

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
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.Default(new MSH());
        msh.setMessageType("ACK");
        msh.setMessageEvent("O21");

        return msh;
    }

    public static MSH Default(String controlID)
    {
        MSH msh = MSH.Default();
        msh.setMessageControlID(controlID);

        return msh;
    }

    @Override
    public String toString() {
        String value = "MSH|^~\\&|" +
                nullSafe(getSendingApplication()) + "|" +                            // 3
                nullSafe(getSendingFacility()) + "|" +                               // 4
                nullSafe(getReceivingApplication()) + "|" +                          // 5
                nullSafe(getReceivingFacility()) + "|" +                             // 6
                nullSafe(getMessageDateHour()) + "||" +                              // 7
                nullSafe(getMessageType()) + "^" + nullSafe(getMessageEvent()) + "^" + nullSafe(getMessageType()) + "|" +    // 9
                nullSafe(getMessageControlID()) + "|" +                              // 10
                nullSafe(getProcessingID()) + "|" +                                  // 11
                nullSafe(getVersionID()) + "|";                                      // 12
        return cleanSegment(value);
    }
}
