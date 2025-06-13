package org.example.domain.host;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HL7Host implements Host {
    LIS("127.0.0.1", 55550, 22220),
    VTG("127.0.0.1", 2222, 5556);

    private final String ip;
    private final int clientPort;
    private final int serverPort;

}


