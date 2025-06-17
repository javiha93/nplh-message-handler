package org.example.domain.host;

import lombok.Data;

import java.util.Map;

@Data
public class HostInfo {
    private String hostId;
    private String hostName;
    private String hostStatus;
    private String originalHostName;
    private String isHL7;
    private String isDriver;
    private Map<String, Connection> connections;
}
