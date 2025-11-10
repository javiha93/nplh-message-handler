package org.example.domain.ws;

public interface WSMessage {
    default String getSoapAction() {
        return null;
    }
}
