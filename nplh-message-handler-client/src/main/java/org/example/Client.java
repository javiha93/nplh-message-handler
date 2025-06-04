package org.example;

public class Client {
    String clientName;

    public void send(String message) { /* Always override */ }
    public String send(String messageType, String message) { /* Always override */  return  null;}
}
