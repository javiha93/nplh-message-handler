package org.example.domain.hl7.VTG.VTGToNPLH.OEWF;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.MessageHeader;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MSH extends org.example.domain.hl7.common.MSH {
    public static MSH Default()
    {
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.Default();
        msh.setMessageType("OML");
        msh.setMessageEvent("O21");

        return msh;
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, String messageType) {
        return (MSH) org.example.domain.hl7.common.MSH.FromMessageHeader(messageHeader, messageType, new MSH());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
