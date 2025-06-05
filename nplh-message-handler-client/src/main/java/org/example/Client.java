package org.example;

import java.util.List;

public class Client {
    String clientName;

    public List<String> send(String message) { /* Always override */ return null;}
    public String send(String messageType, String message) { /* Always override */  return  null;}
}
