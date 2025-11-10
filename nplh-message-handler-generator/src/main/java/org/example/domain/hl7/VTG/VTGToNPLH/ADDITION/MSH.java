package org.example.domain.hl7.VTG.VTGToNPLH.ADDITION;

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
        return  (MSH) org.example.domain.hl7.common.MSH.fromMessageHeader(messageHeader, messageType, new MSH());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
