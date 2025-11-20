package org.example.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.domain.server.message.response.ResponseInfo;
import org.example.domain.server.message.ServerMessage;

import java.util.ArrayList;
import java.util.List;

@Data
public class Server {

    @JsonProperty("serverName")
    protected String serverName;

    @JsonProperty("isRunning")
    protected Boolean isRunning = false;

    @JsonProperty("isDefault")
    protected Boolean isDefault = true;

    @JsonProperty("responses")
    protected List<ResponseInfo> responses = new ArrayList<>();

    @JsonProperty("messages")
    protected List<ServerMessage> messages = new ArrayList<>();


    public void setDefaultResponse(ResponseInfo response) {
        this.responses = new ArrayList<>();
        if (response != null) {
            responses.add(response);
        }
    }

    public  void addResponse(ResponseInfo response) {
        if (responses.isEmpty()) {
            this.responses = new ArrayList<>();
        }
        if (response != null) {
            responses.add(response);
        }
    }

    public ResponseInfo getDefaultResponse() {
        return responses.stream()
                .filter(ResponseInfo::getIsDefault)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found default response"));
    }

    public ResponseInfo getResponseByType(String messageType) {
        return responses.stream()
                .filter(response -> messageType.equals(response.getMessageType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found response with messageType " + messageType));
    }
}
