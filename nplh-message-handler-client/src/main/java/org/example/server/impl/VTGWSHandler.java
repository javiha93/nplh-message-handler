package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VTGWSHandler extends SoapHandler {

    public VTGWSHandler(org.example.logging.MessageLogger messageLogger, String serverName) {
        super(messageLogger, serverName);
    }

    @Override
    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        String soapResponse = buildCommunicationResponse(soapAction);

        //soapResponse = buildApplicationResponse(getTransactionId(messageReceived));

        exchange.getResponseHeaders().set("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, soapResponse.length());
        exchange.getResponseBody().write(soapResponse.getBytes());
        exchange.getResponseBody().close();

        return Collections.singletonList(soapResponse);
    }

    @Override
    protected String getCaseId(String message) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(message)));

            NodeList caseIdList = doc.getElementsByTagName("CaseId");
            if (caseIdList.getLength() > 0) {
                return caseIdList.item(0).getTextContent();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildCommunicationResponse(String soapAction) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.vantage.ventana.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "       <" + soapAction + "Response>\n" +
                "           <" + soapAction + "Result>" +
                "               <IsSuccessful>" + isSuccessful + "</IsSuccessful>\n" +
                "           </" + soapAction + "Result>\n" +
                "       </" + soapAction + "Response>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    private String buildApplicationResponse(String originalTransactionId) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.vantage.ventana.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "       <ProcessApplicationACK>\n" +
                "           <applicationACK>" +
                "                <ErrorCode></ErrorCode>\n" +
                "                <ErrorDescription></ErrorDescription>\n" +
                "                <IsSuccessful>" + isSuccessful + "</IsSuccessful>\n" +
                "                <OriginalTransactionId>" + originalTransactionId + "</OriginalTransactionId>\n" +
                "           </applicationACK>\n" +
                "           <transactionId>" + UUID.randomUUID() + "</transactionId>\n" +
                "       </ProcessApplicationACK>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }

    private String getTransactionId(String requestBody) {
        //<transactionId xsi:type="s:string">63588F63-F4DF-4ABA-A030-F185AC6407DB</transactionId>
        Pattern p = Pattern.compile("<(.+?\\:)?transactionId.*?>(.+?)</(.+?\\:)?transactionId>");
        Matcher m = p.matcher(requestBody);
        if (!m.find()) {
            throw new RuntimeException("expected a transactionId field: " + requestBody);
        }
        return m.group(2);
    }
}
