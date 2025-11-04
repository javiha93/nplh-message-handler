package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import org.example.client.WSClient;
import org.example.domain.CustomResponse;
import org.example.domain.ResponseInfo;
import org.example.domain.ResponseStatus;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.response.CommunicationResponse;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.response.ProcessApplicationACK;
import org.example.server.WSServer;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.example.utils.MessageLogger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.utils.SoapMessageHandler.buildSoapEnvelope;

public class VTGWSHandler extends SoapHandler {

    WSClient client;
    public VTGWSHandler(MessageLogger messageLogger, String serverName, WSServer server, WSClient client ) {
        super(messageLogger, serverName, server);
        this.client = client;

        ResponseInfo defaultResponse = server.getDefaultResponse();

        CustomResponse customResponse = CustomResponse.disabled(buildSoapEnvelope(CommunicationResponse.FromSoapActionOk("soapAction").toString(), "VANTAGE WS"));
        defaultResponse.getCommunicationResponse().setCustomResponse(customResponse);

        customResponse = CustomResponse.disabled(ProcessApplicationACK.FromOriginalTransactionIdOk("*originalControlId*", "*controlId*").toString());
        defaultResponse.getApplicationResponse().setCustomResponse(customResponse);
    }

    @Override
    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        List<String> responses = new ArrayList<>();

        ResponseStatus communicationResponse = server.getDefaultResponse().getCommunicationResponse();
        if (communicationResponse.getIsEnable()) {

            String soapResponse = "";
            if (communicationResponse.getCustomResponse().getUseCustomResponse()) {
                soapResponse = communicationResponse.getCustomResponse().getCustomResponseText();
            } else if (communicationResponse.getIsError()) {
                soapResponse = buildSoapEnvelope(CommunicationResponse.FromSoapActionError(soapAction).toString(), "VANTAGE WS");
            } else {
                soapResponse = buildSoapEnvelope(CommunicationResponse.FromSoapActionOk(soapAction).toString(), "VANTAGE WS");
            }

            exchange.getResponseHeaders().set("Content-Type", "text/xml");
            exchange.sendResponseHeaders(200, soapResponse.length());
            exchange.getResponseBody().write(soapResponse.getBytes());
            exchange.getResponseBody().close();

            responses.add(soapResponse);
        }

        ResponseStatus applicationResponse = server.getDefaultResponse().getApplicationResponse();
        if (applicationResponse.getIsEnable()) {

            String soapResponse = "";
            String transactionId = "";
            if (applicationResponse.getCustomResponse().getUseCustomResponse()) {
                soapResponse = applicationResponse.getCustomResponse().getCustomResponseText();
                soapResponse = soapResponse.replace("*originalControlId*", getTransactionId(messageReceived));
                soapResponse = soapResponse.replace("*controlId*", UUID.randomUUID().toString());
            } else if (applicationResponse.getIsError()) {
                ProcessApplicationACK processApplicationACK = ProcessApplicationACK.FromOriginalTransactionIdError(getTransactionId(messageReceived), applicationResponse.getErrorText());
                soapResponse = processApplicationACK.toString();
                transactionId = processApplicationACK.getTransactionId();
            } else {
                ProcessApplicationACK processApplicationACK = ProcessApplicationACK.FromOriginalTransactionIdOk(getTransactionId(messageReceived));
                soapResponse = processApplicationACK.toString();
                transactionId = processApplicationACK.getTransactionId();
            }
            client.send("ProcessApplicationACK",soapResponse, transactionId);

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
