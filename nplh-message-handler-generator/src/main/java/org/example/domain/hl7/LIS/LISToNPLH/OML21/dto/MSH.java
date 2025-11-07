package org.example.domain.hl7.LIS.LISToNPLH.OML21.dto;

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
        return (MSH) org.example.domain.hl7.common.MSH.Default(new MSH());
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader) {
        return (MSH) org.example.domain.hl7.common.MSH.FromMessageHeader(messageHeader, new MSH());
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, String messageType) {
        MSH msh = (MSH) org.example.domain.hl7.common.MSH.FromMessageHeader(messageHeader, new MSH());

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
