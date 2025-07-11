package org.example.logging;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.catalina.manager.JspHelper.escapeXml;

public class MessageLogger {

    private final Logger delegate;
    private final String clientLogger;

    public MessageLogger(Logger logger, String clientLogger) {
        this.delegate = logger;
        this.clientLogger = clientLogger;
    }

    public void addServerMessage(String caseName, String messageText) {
        StringBuilder sb = new StringBuilder();

        String timestamp = java.time.LocalDateTime.now().toString();

        sb.append("<serverMessage>\n");
        sb.append("  <received>").append(timestamp).append("</received>\n");
        sb.append("  <case>").append(caseName).append("</case>\n");
        sb.append("  <messageText>\n");
        sb.append(indentXml(formatMessageText(messageText), "    "));
        sb.append("\n  </messageText>\n");

        sb.append("</serverMessage>");

        delegate.info(sb.toString());
    }

    public void addServerMessage(String caseName, String messageText, List<String> responses) {
        StringBuilder sb = new StringBuilder();

        String timestamp = java.time.LocalDateTime.now().toString();

        sb.append("<serverMessage>\n");
        sb.append("  <received>").append(timestamp).append("</received>\n");
        sb.append("  <case>").append(caseName).append("</case>\n");
        sb.append("  <messageText>\n");
        sb.append(indentXml(formatMessageText(messageText), "    ")).append("\n");
        sb.append("  </messageText>\n");

        if (!responses.isEmpty()) {
            sb.append("  <responses>\n");
        }

        for (String response : responses) {
            sb.append("    <response>\n");
            sb.append("      <sent>").append(timestamp).append("</sent>\n");
            sb.append("      <messageText>\n");
            sb.append(indentXml(formatMessageText(response), "        ")).append("\n");
            sb.append("      </messageText>\n");
            sb.append("    </response>\n");
        }

        if (!responses.isEmpty()) {
            sb.append("  </responses>\n");
        }

        sb.append("</serverMessage>\n");

        delegate.info(sb.toString());
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

    public void injectReceive(String controlId, String receiveText) {
        try {
            Path logFile = Paths.get("C:/tmp/mocks/client/Client_" + clientLogger + ".log");
            List<String> lines = Files.readAllLines(logFile);

            String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

            List<String> receiveBlock = new ArrayList<>();
            receiveBlock.add("\t" + timestamp + " INFO  - [RECEIVE]: \n");
            receiveBlock.addAll(Arrays.stream(receiveText.split("\r?\n"))
                    .map(line -> "\t" + line)
                    .collect(Collectors.toList()));
            receiveBlock.add(""); // línea en blanco

            List<String> modifiedLines = new ArrayList<>();
            boolean insideTargetBlock = false;
            boolean receiveInserted = false;

            for (String line : lines) {
                if (line.contains("[SEND][" + controlId + "]")) {
                    insideTargetBlock = true;
                }

                if (insideTargetBlock && line.trim().startsWith("####") && !receiveInserted) {
                    modifiedLines.addAll(receiveBlock);
                    receiveInserted = true;
                    insideTargetBlock = false;
                }

                modifiedLines.add(line);
            }

            Files.write(logFile, modifiedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            delegate.error("Error injecting RECEIVE block into log for controlId {}: {}", controlId, e.getMessage(), e);
        }
    }

    private String formatMessageText(String input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(input)));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return stripXmlDeclaration(writer.toString());
        } catch (Exception e) {
            return escapeXml(input);
        }
    }

    private String indentXml(String xml, String indent) {
        return Arrays.stream(xml.split("\n"))
                .map(line -> indent + line)
                .collect(Collectors.joining("\n"));
    }

    private String stripXmlDeclaration(String xml) {
        return xml.replaceFirst("^<\\?xml[^>]+\\?>\\s*", "").strip();
    }




}

