package org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.MSH_ACK;
import org.example.domain.hl7.common.MSH;
import org.example.domain.message.MessageHeader;

@Data
@NoArgsConstructor
public class MSH_SCAN_SLIDE extends MSH {

    public static MSH_SCAN_SLIDE fromMessageHeader(MessageHeader header)
    {
        MSH_SCAN_SLIDE msh = (MSH_SCAN_SLIDE) fromMessageHeader(header, new MSH_SCAN_SLIDE());
        msh.setMessageType("OUL");
        msh.setMessageEvent("R21");
        msh.setSendingApplication("VIRTUOSO");
        msh.setSendingFacility("VENTANA Connect");
        msh.setReceivingApplication("LIS");
        msh.setReceivingFacility("APLAB");

        return msh;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    protected static MSH_SCAN_SLIDE parseMSH(String line) {
        return (MSH_SCAN_SLIDE) MSH.parseMSH(line, new MSH_SCAN_SLIDE());
    }
}
