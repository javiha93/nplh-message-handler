package org.example.domain.hl7.LIS.LISToNPLH.MFQM1;

import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;

public class QRD extends HL7Segment {
    @HL7Position(position = 1)
    private String messageDateHour;

    public static QRD Default() {
        QRD qrd = new QRD();
        qrd.messageDateHour = "messageDateHour";

        return qrd;
    }

    @Override
    public String toString() {
        String value = "QRD|" +
                nullSafe(messageDateHour) + "|" +                            // 3
                "R|" +                               // 4
                "I|||||" +                          // 5
                "PROTOCOL^Staining Protocols^VMS|";
        return cleanSegment(value);
    }

//    MSH|^~\&|LIS|AP Lab|VCONNECT|AP Lab|20250220160722||MFQ^M01|MCI-682e51ce3d2143efb1ac69c04abec14e|P|2.4|
//    QRD|20250220160722|R|I|||||PROTOCOL^Staining Protocols^VMS|
}
