package org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.MessageHeader;

@Data
@NoArgsConstructor
public class MSH extends org.example.domain.hl7.common.MSH {
    public static MSH Default()
    {
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.Default();
        msh.setMessageType("OUL");
        msh.setMessageEvent("R21");

        return msh;
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader) {
        return (MSH) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, new MSH());
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, String messageType) {
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, new MSH());

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
