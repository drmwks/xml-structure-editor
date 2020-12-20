package ru.drmwks;

import javax.xml.stream.*;
import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class StaxExample {
    private StaxExample() {}

    private static final Map<String, String> TAGS_FOR_RENAME = Map.of("note", "reminder");

    private static final Set<String> TAGS_FOR_REMOVE = Set.of("heading");

    public static String processData(String xml) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();

        try (InputStream targetStream = new ByteArrayInputStream(xml.getBytes());
             OutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            XMLStreamReader xmlr = xmlInputFactory.createXMLStreamReader(targetStream, UTF_8.name());
            XMLStreamWriter xmlw = xmlOutputFactory.createXMLStreamWriter(byteArrayOutputStream, UTF_8.name());

            String currentNodeLocalName = null;
            while(xmlr.hasNext()){
                currentNodeLocalName = printEvent(xmlr, xmlw, currentNodeLocalName);
                xmlr.next();
            }

            xmlw.close();
            xmlr.close();
            return byteArrayOutputStream.toString();
        } catch (IOException | XMLStreamException e) {
            System.out.println("Error occuried during processing" + e);
            return null;
        }
    }

    private static String printEvent(XMLStreamReader xmlr, XMLStreamWriter xmlw, String currentNodeLocalName) throws XMLStreamException {
        switch (xmlr.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                currentNodeLocalName = xmlr.getLocalName();
                if (!TAGS_FOR_REMOVE.contains(currentNodeLocalName)) {
                    xmlw.writeStartElement(replaceName(currentNodeLocalName));
                    writeNamespaces(xmlr, xmlw);
                    writeAttributes(xmlr, xmlw);

                    if (currentNodeLocalName.equals("note")) {
                        xmlw.writeCharacters("\n");
                        xmlw.writeStartElement("time");
                        xmlw.writeCharacters(new Date().toString());
                        xmlw.writeEndElement();
                    }
                }
                break;
            case XMLStreamConstants.END_ELEMENT:
                if (!TAGS_FOR_REMOVE.contains(xmlr.getLocalName())) {
                    xmlw.writeEndElement();
                }
                break;
            case XMLStreamConstants.SPACE:
            case XMLStreamConstants.CHARACTERS:
                if (currentNodeLocalName == null || !TAGS_FOR_REMOVE.contains(currentNodeLocalName)) {
                    int start = xmlr.getTextStart();
                    int length = xmlr.getTextLength();
                    xmlw.writeCharacters(new String(xmlr.getTextCharacters(), start, length));
                }
                break;
            case XMLStreamConstants.CDATA:
                int start = xmlr.getTextStart();
                int length = xmlr.getTextLength();
                xmlw.writeCData(new String(xmlr.getTextCharacters(), start, length));
                break;
            case XMLStreamConstants.COMMENT:
                // Skip comments
                break;
            case XMLStreamConstants.START_DOCUMENT:
                xmlw.writeStartDocument();
                xmlw.writeCharacters("\n");
                break;
            case XMLStreamConstants.END_DOCUMENT:
                xmlw.writeEndDocument();
                break;
        }
        return currentNodeLocalName;
    }

    private static String replaceName(String name) {
        return TAGS_FOR_RENAME.getOrDefault(name, name);
    }

    private static void writeAttributes(XMLStreamReader xmlr, XMLStreamWriter xmlw) throws XMLStreamException {
        for (int i=0; i < xmlr.getAttributeCount(); i++) {
            xmlw.writeAttribute(xmlr.getAttributePrefix(i), xmlr.getAttributeNamespace(i),
                    xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));
        }
    }

    private static void writeNamespaces(XMLStreamReader xmlr, XMLStreamWriter xmlw) throws XMLStreamException {
        for (int i=0; i < xmlr.getNamespaceCount(); i++) {
            xmlw.writeNamespace(xmlr.getNamespacePrefix(i), xmlr.getNamespaceURI(i));
        }
    }
}
