package org.example.utils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {

    public static List<String> extractUUIDs(String message) {
        List<String> uuids = new ArrayList<>();
        Pattern uuidPattern = Pattern.compile(
                "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = uuidPattern.matcher(message);
        while (matcher.find()) {
            uuids.add(matcher.group());
        }

        return uuids;
    }

    public static String textToLlp(String textMessage) {
        return HL7LLPCharacters.VT.getCharacter()
                + textMessage.trim().replaceAll("\\n", HL7LLPCharacters.CR.getCharacterAsString())
                + HL7LLPCharacters.CR.getCharacter()
                + HL7LLPCharacters.FS.getCharacter()
                + HL7LLPCharacters.CR.getCharacter();
    }

    public static String llpToText(String llpMessage) {
        if (llpMessage == null || llpMessage.isEmpty()) {
            return llpMessage;
        }

        String trimmedMessage = llpMessage.startsWith(HL7LLPCharacters.VT.getCharacterAsString())
                ? llpMessage.substring(1)
                : llpMessage;

        int fsIndex = trimmedMessage.indexOf(HL7LLPCharacters.FS.getCharacter());
        if (fsIndex >= 0) {
            trimmedMessage = trimmedMessage.substring(0, fsIndex);
        }

        return trimmedMessage.replaceAll(HL7LLPCharacters.CR.getCharacterAsString(), "\n").trim();
    }

    public static String formatXml(String inputXml) {
        try {
            // Parseamos el string a DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);  // para manejar bien los espacios de nombres
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(inputXml)));

            // Transformamos el DOM a String con indentación
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // indentación con 4 espacios

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();

        } catch (Exception e) {
            // Si algo va mal, devolvemos el texto original (sin formatear)
            return inputXml;
        }
    }

}
