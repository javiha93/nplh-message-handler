package org.example.domain.host;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RESTHost implements Host {
    private String hostName;
    private String apiKey;
    @Override
    public String name() {
        return getHostName();
    }
}
