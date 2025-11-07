package org.example.domain.hl7.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.MessageHeader;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MSH extends HL7Segment {

    @HL7Position(position = 3)
    private String sendingApplication;

    @HL7Position(position = 4)
    private String sendingFacility;

    @HL7Position(position = 5)
    private String receivingApplication;

    @HL7Position(position = 6)
    private String receivingFacility;

    @HL7Position(position = 7)
    private String messageDateHour;

    @HL7Position(position = 9, subPosition = 1)
    private String messageType;

    @HL7Position(position = 9, subPosition = 2)
    private String messageEvent;

    @HL7Position(position = 10)
    private String messageControlID;

    @HL7Position(position = 11)
    private String processingID;

    @HL7Position(position = 12)
    private String versionID;

    public static MSH Default()
    {
        MSH msh = new MSH();
        msh.sendingApplication = "LIS";
        msh.sendingFacility = "XYZ Laboratory";
        msh.receivingApplication = "Ventana";
        msh.receivingFacility = "ABC Laboratory";
        msh.messageDateHour = "20240518170503";
        msh.messageControlID = UUID.randomUUID().toString();
        msh.processingID = "P";
        msh.versionID = "2.4";

        return msh;
    }

    public static MSH Default(String controlId)
    {
        MSH msh = MSH.Default();
        msh.setMessageControlID(controlId);

        return msh;
    }

    public static MSH Default(MSH msh)
    {
        msh.sendingApplication = "LIS";
        msh.sendingFacility = "XYZ Laboratory";
        msh.receivingApplication = "Ventana";
        msh.receivingFacility = "ABC Laboratory";
        msh.messageDateHour = "20240518170503";
        msh.messageControlID = UUID.randomUUID().toString();
        msh.processingID = "P";
        msh.versionID = "2.4";

        return msh;
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, MSH msh) {
        msh.sendingApplication = messageHeader.getSendingApplication();
        msh.sendingFacility = messageHeader.getSendingFacility();
        msh.receivingApplication = messageHeader.getReceivingApplication();
        msh.receivingFacility = messageHeader.getReceivingFacility();
        msh.messageDateHour = messageHeader.getMessageDateTime();
        msh.messageControlID = messageHeader.getMessageControlId();
        msh.processingID = messageHeader.getProcessingId();
        msh.messageType = messageHeader.getMessageType();
        msh.messageEvent = messageHeader.getMessageEvent();
        msh.versionID = messageHeader.getVersionId();

        return msh;
    }

    public static MSH FromMessageHeader(MessageHeader messageHeader, String messageType, MSH msh) {
        msh = FromMessageHeader(messageHeader, msh);

        String[] messageTypeParts = messageType.split("\\^");
        msh.setMessageType(messageTypeParts[0]);
        msh.setMessageEvent(messageTypeParts[1]);

        return msh;
    }

    @Override
    public String toString() {
        String value = "MSH|^~\\&|" +
                nullSafe(sendingApplication) + "|" +                            // 3
                nullSafe(sendingFacility) + "|" +                               // 4
                nullSafe(receivingApplication) + "|" +                          // 5
                nullSafe(receivingFacility) + "|" +                             // 6
                nullSafe(messageDateHour) + "||" +                              // 7
                nullSafe(messageType) + "^" + nullSafe(messageEvent) + "|" +    // 9
                nullSafe(messageControlID) + "|" +                              // 10
                nullSafe(processingID) + "|" +                                  // 11
                nullSafe(versionID) + "|";                                      // 12
        return cleanSegment(value);
    }
}
