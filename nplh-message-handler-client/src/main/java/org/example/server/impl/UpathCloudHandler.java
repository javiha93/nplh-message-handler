package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.example.server.WSServer;
import org.example.utils.MessageLogger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class UpathCloudHandler extends SoapHandler {

    public UpathCloudHandler(MessageLogger messageLogger, String serverName, WSServer server) {
        super(messageLogger, serverName, server);
    }

    @Override
    protected String buildResponse(HttpExchange exchange, String soapAction) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.virtuoso.ventana.com/\">" +
                "   <soapenv:Header/>" +
                "   <soapenv:Body>" +
                "      <web:" + soapAction + "Response>" +
                "         <return>" +
                "            <errorCode></errorCode>" +
                "            <errorMessage></errorMessage>" +
                "            <succeed>"+ isSuccessful + "</succeed>" +
                "         </return>" +
                "      </web:" + soapAction + "Response>" +
                "   </soapenv:Body>" +
                "</soapenv:Envelope>";
    }

    @Override
    protected String getCaseId(String message) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(message)));

            NodeList caseIdList = doc.getElementsByTagName("accessionNumber");
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
