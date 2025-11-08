package org.example.domain.hl7;

public interface HL7Message {
    default String getControlId() {
        throw new UnsupportedOperationException(
                "Conversion not supported for message type: " + this
        );
    }
}
