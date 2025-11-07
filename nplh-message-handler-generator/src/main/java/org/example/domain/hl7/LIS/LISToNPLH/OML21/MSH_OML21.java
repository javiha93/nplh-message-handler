package org.example.domain.hl7.LIS.LISToNPLH.OML21;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.MSH;
import org.example.domain.message.MessageHeader;

@Data
@NoArgsConstructor
public class MSH_OML21 extends MSH {
    public static MSH_OML21 Default()
    {
        return (MSH_OML21) MSH.Default(new MSH_OML21());
    }

    public static MSH_OML21 FromMessageHeader(MessageHeader messageHeader) {
        return (MSH_OML21) MSH.FromMessageHeader(messageHeader, new MSH_OML21());
    }

    public static MSH_OML21 FromMessageHeader(MessageHeader messageHeader, String messageType) {
        MSH_OML21 msh = (MSH_OML21) MSH.FromMessageHeader(messageHeader, new MSH_OML21());

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
