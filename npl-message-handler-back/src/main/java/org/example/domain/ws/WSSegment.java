
package org.example.domain.ws;

import java.util.function.Supplier;

public class WSSegment {

    protected String nullSafe(String value) {
        return value == null ? "" : value;
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
}
