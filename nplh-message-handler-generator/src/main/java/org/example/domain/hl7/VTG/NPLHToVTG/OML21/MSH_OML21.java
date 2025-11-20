package org.example.domain.hl7.VTG.NPLHToVTG.OML21;

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
        return (MSH_OML21) MSH.fromMessageHeader(messageHeader, new MSH_OML21());
    }

    public static MSH_OML21 FromMessageHeader(MessageHeader messageHeader, String messageType) {
        MSH_OML21 msh = (MSH_OML21) MSH.fromMessageHeader(messageHeader, new MSH_OML21());

        String[] messageTypeParts = messageType.split("\\^");
        msh.setMessageType(messageTypeParts[0]);
        msh.setMessageEvent(messageTypeParts[1]);
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

    protected static MSH_OML21 parseMSH(String line) {
        MSH_OML21 msh = (MSH_OML21) MSH.parseMSH(line, new MSH_OML21());

        if (!msh.getMessageType().equals("OML") || !msh.getMessageEvent().equals("O21")) {
            throw new RuntimeException("Unable to parse to OML^O21");
        }

        return msh;
    }
}
