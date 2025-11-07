package org.example.utils;

import org.example.service.IrisService;
import org.slf4j.Logger;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MessageLogger {

    private final Logger delegate;
    private final String name;
    private final MockType mockType;
    private final String BASE_LOG_PATH;
    private static String BASE_SERVER_LOG_PATH = "C:/tmp/mocks/server/";
    private static String LOG_FILE_ROUTE;

    public MessageLogger(Logger logger, IrisService irisService, String name, MockType mockType) {
        this.delegate = logger;
        this.name = name;
        this.mockType = mockType;
        this.BASE_LOG_PATH = irisService.getSharingPath();
    }

    // === Public Logging APIs ===

    public void addClientMessage(String caseName,  String controlId, String messageText) {
        addClientMessage(caseName, controlId, messageText, List.of());
    }

    public void addClientMessage(String caseName, String controlId, String messageText, List<String> responses) {
        String timestamp = now();
        XElement root = new XElement("clientMessage")
                .addChild("received", timestamp)
                .addChild("case", caseName)
                .addChild("controlId", controlId)
                .addChild("messageText", formatMessageText(messageText), false);

        if (!responses.isEmpty()) {
            XElement responsesElem = new XElement("responses");
            for (String response : responses) {
                responsesElem.addChild(
                        new XElement("response")
                                .addChild("sent", timestamp)
                                .addChild("messageText", response, true)
                );
            }
            root.addChild(responsesElem);
        }

        writeEntryToXmlLog(root.render(), true);
    }

    public void addServerMessage(String caseName, String messageText) {
        addServerMessage(caseName, messageText, List.of());
    }

    public void addServerMessage(String caseName, String messageText, List<String> responses) {
        String timestamp = now();
        XElement root = new XElement("serverMessage")
                .addChild("sent", timestamp)
                .addChild("case", caseName)
                .addChild("messageText", messageText, true);

        if (!responses.isEmpty()) {
            XElement responsesElem = new XElement("responses");
            for (String response : responses) {
                responsesElem.addChild(
                        new XElement("response")
                                .addChild("received", timestamp)
                                .addChild("messageText", response, true)
                );
            }
            root.addChild(responsesElem);
        }

        writeEntryToXmlLog(root.render(), false);
    }

    public void addResponse(String controlId, String message) {
        Path logFile = getLogFilePath();

        try {
            XElement root = XElement.parse(Files.readString(logFile));

            for (XElement clientMessage : root.children) {
                XElement messageText = clientMessage.getChild("messageText");
                messageText.setText(escapeXml(messageText.text));
            }

            Optional<XElement> clientMessageOpt = root.children.stream()
                    .filter(e -> "clientMessage".equals(e.name))
                    .filter(e -> controlId.equals(e.getChildText("controlId")))
                    .findFirst();

            if (clientMessageOpt.isEmpty()) {
                throw new RuntimeException("No clientMessage with controlId=" + controlId);
            }

            XElement clientMessage = clientMessageOpt.get();
            XElement responses = clientMessage.getChild("responses");

            if (responses == null) {
                responses = new XElement("responses");
                clientMessage.addChild(responses);
            }

            for (XElement response : responses.children) {
                XElement messageText = response.getChild("messageText");
                messageText.setText(escapeXml(messageText.text));
            }

            XElement response = new XElement("response")
                    .addChild("received", now())
                    .addChild("messageText", formatMessageText(message), false);

            responses.addChild(response);

            Files.writeString(logFile, root.render());
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing XML log file", e);
        }
    }

    public void info(String format, Object... arguments) {
        delegate.info(format, arguments);
    }

    public void error(String format, Object... arguments) {
        delegate.error(format, arguments);
    }

    public void debug(String format, Object... arguments) {
        delegate.debug(format, arguments);
    }

    public void warn(String format, Object... arguments) {
        delegate.warn(format, arguments);
    }

    // === Private Helpers ===

    private Path getLogFilePath() {
        if (mockType.equals(MockType.CLIENT)) {
            return Paths.get( BASE_LOG_PATH + "mocks/client/Client_" + name + ".xml");
        } else if (mockType.equals(MockType.SERVER)) {
            return Paths.get(LOG_FILE_ROUTE = BASE_LOG_PATH + "mocks/server/Server_" + name + ".xml");
        } else {
            throw new RuntimeException("Wrong mockType: " + mockType.toString());
        }
    }

    private Path getServerLogFilePath() {
        return Paths.get(BASE_SERVER_LOG_PATH + "Server_" + name + ".xml");
    }

    private String now() {
        return LocalDateTime.now().toString();
    }

    private void writeEntryToXmlLog(String messageXml, boolean isClient) {
        Path logFile =getLogFilePath();
//        if (isClient) {
//            logFile = getClientLogFilePath();
//        } else {
//            logFile = getServerLogFilePath();
//        }


        try {
            if (!Files.exists(logFile)) {
                messageXml = messageXml.trim() + "\n</messages>";
                delegate.info(messageXml);
                return;
            }

            List<String> lines = Files.readAllLines(logFile);

            if (!lines.isEmpty() && lines.get(lines.size() - 1).trim().equals("</messages>")) {
                lines.remove(lines.size() - 1);
            }

            lines.add(messageXml.trim());
            lines.add("</messages>");

            Files.write(logFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            delegate.error("Error writing XML log: {}", e.getMessage(), e);
        }
    }

    private String formatMessageText(String input) {
        // Escapar entidades XML comunes, mantener saltos de línea para legibilidad
        return Arrays.stream(input.split("\n"))
                .map(line -> escapeXml(line))
                .collect(Collectors.joining("\n"));
    }

    private String escapeXml(String s) {
        if (s == null) return null;
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

}
