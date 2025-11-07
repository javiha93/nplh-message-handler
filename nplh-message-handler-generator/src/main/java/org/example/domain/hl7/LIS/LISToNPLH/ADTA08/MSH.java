package org.example.domain.hl7.LIS.LISToNPLH.ADTA08;

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
        msh.setMessageType("ADT");
        msh.setMessageEvent("A08");

        return msh;
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, String messageType) {
        return (MSH) MSH.FromMessageHeader(messageHeader, messageType, new MSH());
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
