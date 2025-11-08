package org.example.domain.hl7.LIS.LISToNPLH.response.ACK;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.MSH;

import java.util.Objects;

@Data
@NoArgsConstructor
public class MSH_ACK extends MSH {

    public static MSH_ACK Default()
    {
        MSH_ACK msh = (MSH_ACK) Default(new MSH_ACK());
        msh.setMessageType("ACK");
        msh.setMessageEvent("O21");
        msh.setSendingApplication("Roche Diagnostics");
        msh.setSendingFacility("VENTANA Connect");
        msh.setReceivingApplication("LIS");
        msh.setReceivingFacility("APLAB");

        return msh;
    }

    public static MSH_ACK Default(String controlID)
    {
        MSH_ACK msh = MSH_ACK.Default();
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

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    protected static MSH_ACK parseMSH(String line) {
        return (MSH_ACK) MSH.parseMSH(line, new MSH_ACK());
    }
}
