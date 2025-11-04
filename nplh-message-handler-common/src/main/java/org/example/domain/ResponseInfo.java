package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInfo {

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("isDefault")
    private Boolean isDefault;

    @JsonProperty("applicationResponse")
    private ResponseStatus applicationResponse;

    @JsonProperty("communicationResponse")
    private ResponseStatus communicationResponse;

    public static ResponseInfo createDefault(ResponseStatus applicationResponse, ResponseStatus communicationResponse) {
        return new ResponseInfo(
                "DEFAULT",
                true,
                applicationResponse,
                communicationResponse
        );
    }

    public static ResponseInfo createDefault() {
        return new ResponseInfo(
                "DEFAULT",
                true,
                ResponseStatus.disabled(),
                ResponseStatus.disabled()
        );
    }

    public static ResponseInfo createCustomResponse(String messageType, ResponseStatus applicationResponse, ResponseStatus communicationResponse) {
        return new ResponseInfo(
                messageType,
                false,
                applicationResponse,
                communicationResponse
        );
    }
}
