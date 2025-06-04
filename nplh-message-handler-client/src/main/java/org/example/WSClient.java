package org.example;

import org.example.domain.host.WSHost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class WSClient extends Client {
    private final String baseUrl;
    private final Map<String, String> headers;

    public WSClient(WSHost host) {
        this.clientName = host.name();
        this.baseUrl = "http://127.0.0.1:" + 80 + host.getPath();
        this.headers = host.getHeader() != null ? host.getHeader() : new HashMap<>();
    }

    @Override
    public String send(String soapAction, String messageBody) {
        String requestBody = buildSoapEnvelope(messageBody);

        try {
            URL oURL = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) oURL.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
            con.setRequestProperty("SOAPAction", soapAction);

            if (headers != null) {
                for (var header : headers.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }
            }

            OutputStream reqStream = con.getOutputStream();
            reqStream.write(requestBody.getBytes());

            StringBuilder r = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                r.append(line);
            }
            br.close();

            return r.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String buildSoapEnvelope(String messageBody) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.virtuoso.ventana.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                messageBody + "\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
}
