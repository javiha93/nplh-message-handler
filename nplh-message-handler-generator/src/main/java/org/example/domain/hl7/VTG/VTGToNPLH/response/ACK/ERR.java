package org.example.domain.hl7.VTG.VTGToNPLH.response.ACK;

import lombok.Data;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

import java.util.Objects;

@Data
public class ERR extends HL7Segment {

    @HL7Position(position = 1)
    private String errorCondition;

    @HL7Position(position = 4)
    private String textMessage;

    public static ERR FromError(String error) {
        ERR err = new ERR();
        err.errorCondition = "ErrorCondition";
        err.textMessage = error;

        return err;
    }

    @Override
    public String toString() {
        String value = "ERR|||" +
                nullSafe(errorCondition) + "|E||" +              // 3
                nullSafe(textMessage) + "|";                    // 7
        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ERR err = (ERR) o;
        return Objects.equals(errorCondition, err.errorCondition) &&
                Objects.equals(textMessage, err.textMessage);
    }

    protected static ERR parseERR(String line) {
        ERR err = new ERR();
        String[] fields = line.split("\\|");

        if (fields.length > 3) err.setErrorCondition(getFieldValue(fields, 3));
        if (fields.length > 7) err.setTextMessage(getFieldValue(fields, 7));

        return err;
    }
}
