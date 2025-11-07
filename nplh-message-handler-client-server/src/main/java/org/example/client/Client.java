package org.example.client;

import lombok.Getter;
import org.example.domain.host.HostType;

@Getter
public abstract  class Client {
    protected String clientName;
    protected HostType clientType;
}
