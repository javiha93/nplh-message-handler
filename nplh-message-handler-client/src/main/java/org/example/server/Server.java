package org.example.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Server {

    @JsonProperty("serverName")
    protected String serverName;

    @JsonProperty("isRunning")
    protected Boolean isRunning = false;

    @JsonProperty("applicationResponse")
    protected Boolean applicationResponse = false;
    
    @JsonProperty("communicationResponse")
    protected Boolean communicationResponse = false;

    // Getters públicos para Jackson
    public String getServerName() {
        return serverName;
    }

    public Boolean getIsRunning() {
        return isRunning;
    }

    public Boolean getApplicationResponse() {
        return applicationResponse;
    }

    public Boolean getCommunicationResponse() {
        return communicationResponse;
    }

    // Setters para completar el JavaBean pattern
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setIsRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setApplicationResponse(Boolean applicationResponse) {
        this.applicationResponse = applicationResponse;
    }

    public void setCommunicationResponse(Boolean communicationResponse) {
        this.communicationResponse = communicationResponse;
    }
}
