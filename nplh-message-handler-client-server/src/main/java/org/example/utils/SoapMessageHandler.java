package org.example.utils;

public class SoapMessageHandler {

    public static String buildSoapEnvelope(String messageBody, String hostName) {
        String hostWeb = switch (hostName) {
            case "VSS" -> "vss";
            case "DP600" -> "dp600";
            case "VANTAGE WS" -> "vantage";
            default -> "virtuoso";
        };

        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice." + hostWeb + ".ventana.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                messageBody + "\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
