package org.example.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {
    
    @JsonProperty("isEnable")
    private Boolean isEnable;
    
    @JsonProperty("isError")
    private Boolean isError;

    @JsonProperty("errorText")
    private String errorText;

    @JsonProperty("customResponse")
    private CustomResponse customResponse;
    
    // Constructor de conveniencia para crear estados comunes
    public static ResponseStatus enabled() {
        return new ResponseStatus(true, false, "", CustomResponse.empty());
    }
    
    public static ResponseStatus disabled() {
        return new ResponseStatus(false, false, "", CustomResponse.empty());
    }
    
    public static ResponseStatus enabledWithError(String errorText) {
        return new ResponseStatus(true, true, errorText, CustomResponse.empty());
    }

    public static ResponseStatus enabledWithCustomResponse(String customResponse) {
        return new ResponseStatus(true, true, "", CustomResponse.enabled(customResponse));
    }

    public Boolean hasErrorText() { return !errorText.isEmpty();}

    
    @Override
    public String toString() {
        return String.format("ResponseStatus{isEnable=%s, isError=%s}", isEnable, isError);
    }
}