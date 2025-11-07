package org.example.domain.messageType;

import java.util.Objects;

public class MessageTypeResolver {

    public static MessageType fromString(String type, String hostType) {
        if (type == null || type.isBlank()) {
            return null;
        }

        try {
            return AUTOMATIONSW.valueOf(type);
        } catch (IllegalArgumentException ignored) {}

        try {
            if (Objects.equals(hostType, "DP")) {
                return DP600.valueOf(type);
            }
        } catch (IllegalArgumentException ignored) {}

        try {
            return LIS.valueOf(type);
        } catch (IllegalArgumentException ignored) {}

        try {
            if (Objects.equals(hostType, "VIRTUOSO")) {
                return UPATHCLOUD.valueOf(type);
            }
        } catch (IllegalArgumentException ignored) {}

        try {
            return VSS.valueOf(type);
        } catch (IllegalArgumentException ignored) {}

        try {
            return VTG.valueOf(type);
        } catch (IllegalArgumentException ignored) {}

        try {
            return VTGWS.valueOf(type);
        } catch (IllegalArgumentException ignored) {}

        throw new IllegalArgumentException("Unknown messageType: " + type);
    }
}
