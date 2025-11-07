package org.example.utils;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class XElement {

    public final String name;
    public String text;
    public final List<XElement> children = new ArrayList<>();

    public XElement(String name) {
        this.name = name;
    }

    public XElement(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public XElement addChild(XElement child) {
        this.children.add(child);
        return this;
    }

    public XElement addChild(String name, String text) {
        this.children.add(new XElement(name, text));
        return this;
    }

    public XElement addChild(String name, String text, boolean formatXml) {
        if (formatXml) {
            this.children.add(new XElement(name, formatXmlSafe(text)));
        } else {
            this.children.add(new XElement(name, text));
        }
        return this;
    }

    public XElement getChild(String tag) {
        return children.stream()
                .filter(e -> e.name.equals(tag))
                .findFirst()
                .orElse(null);
    }

    public String getChildText(String tag) {
        XElement child = getChild(tag);
        return child != null ? child.text : null;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public String render() {
        return render("");
    }

    public String render(String indent) {
        StringBuilder sb = new StringBuilder();
        String innerIndent = indent + "  ";

        sb.append(indent).append("<").append(name).append(">");

        if ((text == null || text.isBlank()) && children.isEmpty()) {
            sb.append("</").append(name).append(">\n");
            return sb.toString();
        }

        sb.append("\n");

        if (text != null && !text.isBlank()) {
            if ("messageText".equals(name)) {
                sb.append(indentXml(text, innerIndent)).append("\n");
            } else {
                sb.append(innerIndent).append(text).append("\n");
            }
        }

        for (XElement child : children) {
            sb.append(child.render(innerIndent));
        }

        sb.append(indent).append("</").append(name).append(">\n");
        return sb.toString();
    }

    public static XElement parse(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            return fromDom(doc.getDocumentElement());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }

    private static XElement fromDom(Element elem) {
        XElement xel = new XElement(elem.getTagName());

        NodeList nodes = elem.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);

            if (n instanceof Text txt && !txt.getTextContent().trim().isEmpty()) {
                xel.text = txt.getTextContent().trim();
            } else if (n instanceof Element childElem) {
                xel.addChild(fromDom(childElem));
            }
        }

        return xel;
    }

    private static String indentXml(String xml, String indent) {
        return Arrays.stream(xml.split("\n"))
                .map(line -> indent + line)
                .collect(Collectors.joining("\n"));
    }

    private static String formatXmlSafe(String input) {
        try {
            var db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            var doc = db.parse(new InputSource(new StringReader(input)));

            var transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            StringWriter writer = new StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(doc), new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString().replaceFirst("^<\\?xml[^>]+\\?>\\s*", "").strip();
        } catch (Exception e) {
            return org.apache.catalina.manager.JspHelper.escapeXml(input);
        }
    }
}

