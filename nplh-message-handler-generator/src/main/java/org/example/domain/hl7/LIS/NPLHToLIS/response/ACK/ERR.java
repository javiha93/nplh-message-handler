package org.example.domain.hl7.LIS.NPLHToLIS.response.ACK;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

public class ERR extends HL7Segment {

    @HL7Position(position = 1)
    private String errorCondition;

    @HL7Position(position = 4)
    private String textMessage;

    public static ERR Default() {
        ERR err = new ERR();
        err.errorCondition = "ErrorCondition";
        err.textMessage = "TextMessage";

        return err;
    }

    @Override
    public String toString() {
        String value = "ERR|||" +
                nullSafe(errorCondition) + "|E||" +              // 3
                nullSafe(textMessage) + "|";                    // 7
        return cleanSegment(value);
    }
}
