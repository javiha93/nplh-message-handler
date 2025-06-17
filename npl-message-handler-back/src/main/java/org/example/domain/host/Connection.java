package org.example.domain.host;

import lombok.Data;

@Data
public class Connection {
    private String id;
    private String connectionName;
    private int connectionType;
    private String direction;
    private Integer port;
    private String wsLocation;
    private String apiKeyFile;
    private String apiKeyValue;
}
