package org.example;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StatisticsXmlWriter {
    public static void write(Map<String, Integer> statistics, String parameter)
            throws XMLStreamException, TransformerException, IOException {
        StringWriter rawXML = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(rawXML);
        xmlStreamWriter.writeStartDocument();
        xmlStreamWriter.writeStartElement("statistics");
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            xmlStreamWriter.writeStartElement("item");
            xmlStreamWriter.writeStartElement("value");
            xmlStreamWriter.writeCharacters((entry.getKey()));
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeStartElement("count");
            xmlStreamWriter.writeCharacters(entry.getValue().toString());
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndElement();
        }
        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.flush();
        xmlStreamWriter.close();
        String unformattedXML = rawXML.toString();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        StringWriter formattedXML = new StringWriter();
        transformer.transform(
                new StreamSource(new StringReader(unformattedXML)),
                new StreamResult(formattedXML)
        );
        Files.writeString(Path.of("statistics_by_" + parameter + ".xml"),
                formattedXML.toString(), StandardCharsets.UTF_8);
    }
}
