package org.example;

import org.example.domain.host.ClientMessageResponse;

import java.util.List;

public class Client {
    String clientName;

    public List<ClientMessageResponse> send(String message, String controlId) { /* Always override */ return null;}
    public String send(String messageType, String message, String controlId) { /* Always override */  return  null;}
}
