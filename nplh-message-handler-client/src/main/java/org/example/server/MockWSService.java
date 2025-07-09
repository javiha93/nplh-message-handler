package org.example.server;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public class MockWSService {
    @WebMethod
    public String receive(String input) {
        return "";
    }
}
