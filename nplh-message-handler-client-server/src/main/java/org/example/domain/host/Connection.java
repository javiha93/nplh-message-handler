package org.example.domain.host;

import lombok.Data;

@Data
public class Connection {
    private String id;
    private String connectionName;
    private String direction;
    private String status;
    private String ip;
    private Integer port;
    private String path;
    private String wsName;
    private String wsLocation;
    private String apiKeyFile;
    private String apiKeyValue;
    private String apiKeyTag;
}
