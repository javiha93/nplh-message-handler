package org.example.domain.server;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ResponseStatus;

@Data
@NoArgsConstructor
public class ServerStatus {

    private String serverName;
    private ResponseStatus communicationResponse;
    private ResponseStatus applicationResponse;
    private Boolean isRunning;

    public ServerStatus(String serverName) {
        this.serverName = serverName;
        this.communicationResponse = ResponseStatus.enabled();
        this.applicationResponse = ResponseStatus.disabled();
        this.isRunning = false;
    }

    public ServerStatus(String serverName, ResponseStatus communicationResponse, ResponseStatus applicationResponse, Boolean isRunning) {
        this.serverName = serverName;
        this.communicationResponse = communicationResponse;
        this.applicationResponse = applicationResponse;
        this.isRunning = isRunning;
    }

    // Factory methods for common states
    public static ServerStatus createDefault(String serverName) {
        return new ServerStatus(serverName);
    }

    public static ServerStatus createRunning(String serverName) {
        ServerStatus status = new ServerStatus(serverName);
        status.setIsRunning(true);
        status.setApplicationResponse(ResponseStatus.enabled());
        return status;
    }

    public static ServerStatus createStopped(String serverName) {
        ServerStatus status = new ServerStatus(serverName);
        status.setIsRunning(false);
        status.setApplicationResponse(ResponseStatus.disabled());
        status.setCommunicationResponse(ResponseStatus.disabled());
        return status;
    }

    public void markAsRunning() {
        this.isRunning = true;
        if (this.communicationResponse != null) {
            this.communicationResponse.setIsEnable(true);
        }
    }

    public void markAsStopped() {
        this.isRunning = false;
        if (this.applicationResponse != null) {
            this.applicationResponse.setIsEnable(false);
        }
        if (this.communicationResponse != null) {
            this.communicationResponse.setIsEnable(false);
        }
    }
}
