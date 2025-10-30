package org.example.server;

import org.example.domain.ResponseStatus;

public interface ServerStatusProvider {
    ResponseStatus getApplicationResponse();
    ResponseStatus getCommunicationResponse();
    void updateApplicationResponse(ResponseStatus status);
    void updateCommunicationResponse(ResponseStatus status);
    boolean isRunning();
}
