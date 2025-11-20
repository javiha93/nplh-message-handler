package org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.MSH;
import org.example.domain.message.MessageHeader;

@Data
@NoArgsConstructor
public class MSH_SlideUpdate extends MSH {
    public static MSH_SlideUpdate Default()
    {
        MSH_SlideUpdate msh = (MSH_SlideUpdate) MSH.Default();
        msh.setMessageType("OUL");
        msh.setMessageEvent("R21");

        return msh;
    }

    public static MSH_SlideUpdate FromMessageHeader(MessageHeader messageHeader) {
        return (MSH_SlideUpdate) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, new MSH_SlideUpdate());
    }

    public static MSH_SlideUpdate FromMessageHeaderVTGSender(MessageHeader messageHeader) {
        MSH_SlideUpdate msh = (MSH_SlideUpdate) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, new MSH_SlideUpdate());
        msh.setSendingApplication("LIS");
        msh.setSendingFacility("APLAB");
        msh.setReceivingApplication("VANTAGE");
        msh.setReceivingFacility("APLAB");

        return msh;
    }

    public static MSH_SlideUpdate FromMessageHeader(MessageHeader messageHeader, String messageType) {
        MSH_SlideUpdate msh = (MSH_SlideUpdate) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, new MSH_SlideUpdate());

        String[] messageTypeParts = messageType.split("\\^");
        msh.setMessageType(messageTypeParts[0]);
        msh.setMessageEvent(messageTypeParts[1]);
        return msh;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
