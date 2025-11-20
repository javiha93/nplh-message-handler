package org.example.domain.hl7.VTG.VTGToNPLH.response.ACK;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.MSH;

import java.util.Objects;

@Data
@NoArgsConstructor
public class MSH_ACK extends org.example.domain.hl7.common.MSH {

    public static MSH_ACK Default()
    {
        MSH_ACK msh = (MSH_ACK) MSH_ACK.Default(new MSH_ACK());
        msh.setMessageType("ACK");
        msh.setSendingApplication("VENTANA Connect");
        msh.setSendingFacility("APLAB");
        msh.setReceivingApplication("VANTAGE");
        msh.setReceivingFacility("APLAB");

        return msh;
    }

    public static MSH_ACK Default(String controlId)
    {
        MSH_ACK msh = (MSH_ACK) MSH_ACK.Default(new MSH_ACK());
        msh.setMessageType("ACK");
        msh.setMessageControlID(controlId);

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
                nullSafe(getMessageType()) + "^^" + nullSafe(getMessageType()) + "|" + // 9
                nullSafe(getMessageControlID()) + "|" +                              // 10
                nullSafe(getProcessingID()) + "|" +                                  // 11
                nullSafe(getVersionID()) + "|";                                      // 12
        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MSH_ACK msh = (MSH_ACK) o;
        return Objects.equals(sendingApplication, msh.sendingApplication) &&
                Objects.equals(sendingFacility, msh.sendingFacility) &&
                Objects.equals(receivingApplication, msh.receivingApplication) &&
                Objects.equals(receivingFacility, msh.receivingFacility) &&
                Objects.equals(messageType, msh.messageType) &&
                Objects.equals(processingID, msh.processingID) &&
                Objects.equals(versionID, msh.versionID);
    }

    protected static MSH_ACK parseMSH(String line) {
        return (MSH_ACK) MSH.parseMSH(line, new MSH_ACK());
    }
}
