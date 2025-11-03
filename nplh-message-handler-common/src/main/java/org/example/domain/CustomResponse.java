package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse {
    @JsonProperty("customResponseText")
    private String customResponseText;

    @JsonProperty("useCustomResponse")
    private Boolean useCustomResponse;


    public static CustomResponse empty() {
        return new CustomResponse("", false);
    }

    public static CustomResponse disabled(String customResponseText) {
        return new CustomResponse(customResponseText, false);
    }

    public static CustomResponse enabled(String customResponseText) {
        return new CustomResponse(customResponseText, true);
    }

    @Override
    public String toString() {
        return "CustomResponse{" +
                "customResponseText='" + customResponseText + '\'' +
                ", useCustomResponse=" + useCustomResponse +
                '}';
    }
}
