package org.example.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.domain.ResponseInfo;
import org.example.domain.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class Server {

    @JsonProperty("serverName")
    protected String serverName;

    @JsonProperty("isRunning")
    protected Boolean isRunning = false;

    @JsonProperty("responses")
    protected List<ResponseInfo> responses = new ArrayList<>();

    @JsonProperty("messages")
    protected List<String> messages = new ArrayList<>();


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
