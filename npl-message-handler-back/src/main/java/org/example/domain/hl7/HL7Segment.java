package org.example.domain.hl7;

public class HL7Segment {

    protected String nullSafe(String value) {
        return value == null ? "" : value;
    }
    protected String nullSafe(HL7Segment segment) {
        return segment == null ? "" : segment.toString();
    }

    protected String cleanSegment(String value) {
        return value.replaceAll("\\^+\\|", "|") // Delete all carets before a pipe
                .replaceAll("\\|+$", "|"); // Delete extra pipes at the end of the message
    }

    protected String cleanMessage(String value) {
        return value.replaceAll("\n+", "\n")  // Replace multiple break for just one
                .replaceAll("\n$", "");   // Delete last break
    }
}
