package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import org.example.domain.hl7.common.MSH;
import org.example.domain.message.MessageHeader;

public class MSH_SLIDE_UPDATE extends MSH {

    public static MSH_SLIDE_UPDATE fromMessageHeader(MessageHeader messageHeader) {
        MSH_SLIDE_UPDATE msh = (MSH_SLIDE_UPDATE) MSH.fromMessageHeader(messageHeader, new MSH_SLIDE_UPDATE());

        msh.setReceivingApplication("LIS");
        msh.setReceivingFacility("APLAB");
        msh.setSendingApplication("Roche Diagnostics");
        msh.setSendingFacility("VENTANA Connect");

        msh.setMessageType("OUL");
        msh.setMessageEvent("R21");

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

    protected static MSH_SLIDE_UPDATE parseMSH(String line) {
        MSH_SLIDE_UPDATE msh = (MSH_SLIDE_UPDATE) MSH.parseMSH(line, new MSH_SLIDE_UPDATE());

        if (!msh.getMessageType().equals("OUL") || !msh.getMessageEvent().equals("R21")) {
            throw new RuntimeException("Unable to parse to OUL^R21");
        }

        return msh;
    }
}
