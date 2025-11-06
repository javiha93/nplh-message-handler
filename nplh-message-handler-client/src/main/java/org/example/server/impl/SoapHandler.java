package org.example.server.impl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.domain.server.ServerMessage;
import org.example.server.WSServer;
import org.example.service.UINotificationService;
import org.example.utils.MessageLogger;
import org.slf4j.MDC;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SoapHandler implements HttpHandler {

    private final MessageLogger messageLogger;
    private final String serverName;
    protected WSServer server;
    protected String messageReceived;
    protected Boolean isSuccessful;

    public SoapHandler(MessageLogger messageLogger, String serverName, WSServer server) {
        this.messageLogger = messageLogger;
        this.serverName = serverName;
        this.server = server;
        this.isSuccessful = true;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        MDC.put("serverLogger", this.serverName);
        messageReceived = new String(exchange.getRequestBody().readAllBytes());

        // Validar SOAP envelope
        ValidationResult validation = validateSoapEnvelope(messageReceived);
        if (!validation.isValid()) {
            messageLogger.warn("SOAP validation failed: {}", validation.getReport());
        } else {
            messageLogger.info("SOAP validation passed");
        }

        //messageLogger.info("[RECEIVE]: \n\n{} \n", messageReceived);
        String soapAction = getSoapAction(exchange);
        List<String> responses = response(exchange, soapAction);

        messageLogger.addServerMessage(getCaseId(messageReceived), messageReceived, responses);

        ServerMessage serverMessage = new ServerMessage(messageReceived, responses);
        server.getMessages().add(serverMessage);
        UINotificationService.addServerMessage(serverName, serverMessage);
    }

    private String getSoapAction(HttpExchange exchange) {
        String fullSoapAction = exchange.getRequestHeaders().getFirst("Soapaction");
        if (fullSoapAction == null || fullSoapAction.isEmpty()) {
            return "";
        }
        
        // Remover comillas si están presentes
        fullSoapAction = fullSoapAction.replace("\"", "");
        
        // Manejar diferentes patrones de URL
        // Patrón 1: http://ventanamed.com/vantage/ventanaconnect/IVCtoVTG_Service/ProcessNewOrder
        // Patrón 2: http://webservice.dp600.ventana.com//ws.ext.DP600.VCToDP600WebService.addSlide
        
        if (fullSoapAction.contains("/")) {
            // Obtener la última parte después del último "/"
            String[] parts = fullSoapAction.split("/");
            String lastPart = parts[parts.length - 1];
            
            // Si la última parte contiene un punto, tomar la parte después del último punto
            if (lastPart.contains(".")) {
                String[] dotParts = lastPart.split("\\.");
                return dotParts[dotParts.length - 1];
            }
            
            return lastPart;
        }
        
        return fullSoapAction;
    }

    protected List<String> response(HttpExchange exchange, String soapAction) throws IOException {
        String soapResponse = "";

        exchange.getResponseHeaders().set("Content-Type", "text/xml");
        exchange.sendResponseHeaders(200, soapResponse.length());
        exchange.getResponseBody().write(soapResponse.getBytes());
        exchange.getResponseBody().close();

        return Collections.singletonList(soapResponse);
    }

    protected String buildResponse(HttpExchange exchange, String soapAction) {
        return null;
    }

    protected String getCaseId(String message) {
        return null;
    }

    /**
     * Valida si el SOAP envelope es correcto
     * @param soapMessage El mensaje SOAP a validar
     * @return ValidationResult con el resultado de la validación
     */
    public ValidationResult validateSoapEnvelope(String soapMessage) {
        ValidationResult result = new ValidationResult();
        
        try {
            // 1. Validar que sea XML válido
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(soapMessage.getBytes()));
            
            // 2. Validar estructura SOAP básica
            Element root = document.getDocumentElement();
            String rootName = root.getLocalName();
            String namespace = root.getNamespaceURI();
            
            // Verificar que el elemento raíz sea Envelope
            if (!"Envelope".equals(rootName)) {
                result.addError("El elemento raíz debe ser 'Envelope', encontrado: " + rootName);
            }
            
            // Verificar namespace SOAP
            if (!"http://schemas.xmlsoap.org/soap/envelope/".equals(namespace)) {
                result.addError("Namespace SOAP incorrecto. Esperado: http://schemas.xmlsoap.org/soap/envelope/, encontrado: " + namespace);
            }
            
            // 3. Validar que tenga Body
            NodeList bodyNodes = root.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
            if (bodyNodes.getLength() == 0) {
                result.addError("El SOAP Envelope debe contener un elemento Body");
            } else if (bodyNodes.getLength() > 1) {
                result.addError("El SOAP Envelope debe contener exactamente un elemento Body");
            }
            
            // 4. Validar Header (opcional pero si existe debe ser único)
            NodeList headerNodes = root.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Header");
            if (headerNodes.getLength() > 1) {
                result.addError("El SOAP Envelope puede contener máximo un elemento Header");
            }
            
            // 5. Validar contenido del Body
            if (bodyNodes.getLength() == 1) {
                Element bodyElement = (Element) bodyNodes.item(0);
                NodeList bodyChildren = bodyElement.getChildNodes();
                
                boolean hasContent = false;
                for (int i = 0; i < bodyChildren.getLength(); i++) {
                    if (bodyChildren.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        hasContent = true;
                        break;
                    }
                }
                
                if (!hasContent) {
                    result.addWarning("El elemento Body está vacío");
                }
            }
            
            result.setValid(result.getErrors().isEmpty());
            
        } catch (Exception e) {
            result.addError("Error al parsear XML: " + e.getMessage());
            result.setValid(false);
        }
        
        return result;
    }
    
    /**
     * Clase para encapsular el resultado de la validación
     */
    public static class ValidationResult {
        private boolean valid = true;
        private List<String> errors = new java.util.ArrayList<>();
        private List<String> warnings = new java.util.ArrayList<>();
        
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        
        public List<String> getErrors() { return errors; }
        public void addError(String error) { this.errors.add(error); }
        
        public List<String> getWarnings() { return warnings; }
        public void addWarning(String warning) { this.warnings.add(warning); }
        
        public String getReport() {
            StringBuilder report = new StringBuilder();
            report.append("SOAP Validation Result: ").append(valid ? "VALID" : "INVALID").append("\n");
            
            if (!errors.isEmpty()) {
                report.append("\nERRORS:\n");
                for (String error : errors) {
                    report.append("  - ").append(error).append("\n");
                }
            }
            
            if (!warnings.isEmpty()) {
                report.append("\nWARNINGS:\n");
                for (String warning : warnings) {
                    report.append("  - ").append(warning).append("\n");
                }
            }
            
            return report.toString();
        }
    }


}
