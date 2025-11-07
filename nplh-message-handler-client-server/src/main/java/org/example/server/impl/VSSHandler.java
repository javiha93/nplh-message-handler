package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.example.domain.server.message.response.ResponseStatus;
import org.example.domain.ws.VSS.VSSToNPLH.response.CommunicationResponse;
import org.example.server.WSServer;
import org.example.utils.MessageLogger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.SoapMessageHandler.buildSoapEnvelope;

public class VSSHandler extends SoapHandler {
    public VSSHandler(MessageLogger messageLogger, String serverName, WSServer server) {
        super(messageLogger, serverName, server);
    }

    @Override
    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        List<String> responses = new ArrayList<>();

        ResponseStatus communicationStatus = server.getDefaultResponse().getCommunicationResponse();
        if (communicationStatus.getIsEnable()) {

            String soapResponse = "";
            if (communicationStatus.getIsError()) {
                if (communicationStatus.hasErrorText()) {
                    soapResponse = buildSoapEnvelope(CommunicationResponse.FromSoapActionError(soapAction, communicationStatus.getErrorText()).toString(), "VSS");
                } else {
                    soapResponse = buildSoapEnvelope(CommunicationResponse.FromSoapActionError(soapAction).toString(), "VSS");
                }
            } else
            {
                soapResponse = buildSoapEnvelope(CommunicationResponse.FromSoapActionOk(soapAction).toString(), "VSS");
            }

            exchange.getResponseHeaders().set("Content-Type", "text/xml");
            exchange.sendResponseHeaders(200, soapResponse.length());
            exchange.getResponseBody().write(soapResponse.getBytes());
            exchange.getResponseBody().close();

            responses.add(soapResponse);
        }

        return responses;
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
