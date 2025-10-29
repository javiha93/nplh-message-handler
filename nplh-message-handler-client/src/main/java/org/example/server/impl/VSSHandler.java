package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.example.utils.MessageLogger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class VSSHandler extends SoapHandler {
    public VSSHandler(MessageLogger messageLogger, String serverName) {
        super(messageLogger, serverName);
    }

    @Override
    protected String buildResponse(HttpExchange exchange, String soapAction) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
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
    }

    @Override
    protected String getCaseId(String message) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(message)));

            NodeList caseIdList = doc.getElementsByTagName("CaseID");
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
}
