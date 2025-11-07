package org.example.domain.client;

import lombok.Getter;
import org.example.domain.host.HostType;

@Getter
public class Client {
    protected String clientName;
    protected HostType clientType;

    public void send(String message, String controlId) { /* Always override */}
    public String send(String messageType, String message, String controlId) { /* Always override */  return  null;}
}
