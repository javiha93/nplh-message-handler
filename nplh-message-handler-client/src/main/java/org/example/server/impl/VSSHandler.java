package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.example.logging.MessageLogger;

import java.io.IOException;

public class VSSHandler extends SoapHandler {
    public VSSHandler(MessageLogger messageLogger, String serverName) {
        super(messageLogger, serverName);
    }

    @Override
    protected void response(HttpExchange exchange, String soapAction) throws IOException {
        String soapResponse = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.vss.ventana.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "       <" + soapAction + "Response>\n" +
                "           <" + soapAction + "Result>" +
                "               <IsSuccessful>" + isSuccessful + "</IsSuccessful>\n" +
                "           </" + soapAction + "Result>\n" +
                "       </" + soapAction + "Response>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

        exchange.getResponseHeaders().set("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, soapResponse.length());
        exchange.getResponseBody().write(soapResponse.getBytes());
        exchange.getResponseBody().close();
    }
}
