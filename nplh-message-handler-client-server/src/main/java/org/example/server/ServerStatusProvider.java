package org.example.server;

import org.example.domain.server.message.response.ResponseStatus;

public interface ServerStatusProvider {
    ResponseStatus getApplicationResponse();
    ResponseStatus getCommunicationResponse();
    void updateApplicationResponse(ResponseStatus status);
    void updateCommunicationResponse(ResponseStatus status);
    boolean isRunning();
}
