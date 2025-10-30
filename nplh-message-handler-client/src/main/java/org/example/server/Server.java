package org.example.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.domain.ResponseStatus;

public class Server {

    @JsonProperty("serverName")
    protected String serverName;

    @JsonProperty("isRunning")
    protected Boolean isRunning = false;

    @JsonProperty("applicationResponse")
    protected ResponseStatus applicationResponse = ResponseStatus.disabled();
    
    @JsonProperty("communicationResponse")
    protected ResponseStatus communicationResponse = ResponseStatus.disabled();

    // Getters públicos para Jackson
    public String getServerName() {
        return serverName;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public ResponseStatus getApplicationResponse() {
        return applicationResponse;
    }

    public ResponseStatus getCommunicationResponse() {
        return communicationResponse;
    }

    // Setters para completar el JavaBean pattern
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setApplicationResponse(ResponseStatus applicationResponse) {
        this.applicationResponse = applicationResponse;
    }

    public void setCommunicationResponse(ResponseStatus communicationResponse) {
        this.communicationResponse = communicationResponse;
    }
}
