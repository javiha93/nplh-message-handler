package org.example.client;

import org.example.domain.host.WSHost;
import org.example.domain.host.host.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WSClient extends Client {

    static final Logger logger = LoggerFactory.getLogger(HL7Client.class);
    private final String baseUrl;
    private Map<String, String> headers = new HashMap<>();

    public WSClient(WSHost host) {
        this.clientName = host.name();
        this.baseUrl = "http://127.0.0.1:" + 80 + host.getPath();
        this.headers = host.getHeader() != null ? host.getHeader() : new HashMap<>();
    }

    public WSClient(String hostName, Connection connection) {
        this.clientName = hostName;
        this.baseUrl = "http://127.0.0.1:" + 80 + connection.getWsName();
        if (!connection.getApiKeyValue().isEmpty()) {
            this.headers = Map.of(connection.getApiKeyFile(), connection.getApiKeyValue());
        }
    }

    @Override
    public String send(String soapAction, String messageBody, String controlId) {
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
            logger.info("\nSent message: \n{}\nto Host {}", messageBody, clientName);

            StringBuilder r = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                r.append(line);
            }
            br.close();

            logger.info("\nReceived response: \n{}\nfrom Host {}",
                    extractSoapResponse(r.toString()),
                    clientName);

            return extractSoapResponse(r.toString());
        } catch (Exception e) {
            logger.error("Error sending message: {} to {}, Error:", messageBody, clientName, e);
            return e.getMessage();
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

    private String extractSoapResponse(String response) {
        Pattern pattern = Pattern.compile("<SOAP-ENV:Body>(.*?)</SOAP-ENV:Body>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            response = matcher.group(1);
        }
        return response.replaceAll("types:", "")
                .replaceAll(" xsi:type\\s*=\\s*\"[^\"]*\"", "");
    }
}
