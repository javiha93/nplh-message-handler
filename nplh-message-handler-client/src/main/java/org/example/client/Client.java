package org.example.client;

import lombok.Getter;
import org.example.client.message.ClientMessageResponse;

import java.util.List;

@Getter
public class Client {
    String clientName;

    public List<ClientMessageResponse> send(String message, String controlId) { /* Always override */ return null;}
    public String send(String messageType, String message, String controlId) { /* Always override */  return  null;}
}
