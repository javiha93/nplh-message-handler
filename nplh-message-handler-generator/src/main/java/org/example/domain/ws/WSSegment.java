
package org.example.domain.ws;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

public class WSSegment {

    protected String nullSafe(String value) {
        return value == null ? "" : value;
    }

    protected String nullSafe(LocalDateTime value) {
        return value == null ? "" : value.toString();
    }

    protected <T extends WSSegment> T nullSafe(T segment, Supplier<T> emptyInstanceSupplier) {
        return segment != null ? segment : emptyInstanceSupplier.get();
    }

    protected String addIndentation(int indentationLevel) {
        StringBuilder indentation = new StringBuilder();
        for (int i = 0; i < indentationLevel; i++) {
            indentation.append("     ");
        }
        return indentation.toString();
    }

    protected static String convertToXmlDateTime(String yyyymmddOrFull) {
        try {

            if (yyyymmddOrFull == null) {
                return null;
            }
            // Si la string tiene 14 dígitos -> yyyyMMddHHmmss
            if (yyyymmddOrFull.length() == 14) {
                LocalDateTime dateTime = LocalDateTime.parse(
                        yyyymmddOrFull,
                        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                );
                return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
            // Si la string tiene 8 dígitos -> yyyyMMdd
            else if (yyyymmddOrFull.length() == 8) {
                LocalDate date = LocalDate.parse(
                        yyyymmddOrFull,
                        DateTimeFormatter.ofPattern("yyyyMMdd")
                );
                LocalDateTime dateTime = date.atStartOfDay(); // 00:00:00
                return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
            else {
                throw new IllegalArgumentException("Formato de fecha no soportado: " + yyyymmddOrFull);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error parseando la fecha: " + yyyymmddOrFull, e);
        }
    }
}
