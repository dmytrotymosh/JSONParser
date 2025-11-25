package org.example;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException, XMLStreamException, TransformerException {
        if (!new File("./data").exists()) {
            Path folder = Paths.get("./data");
            Files.createDirectory(folder);
            System.out.println("Test data generating...");
            JsonFileGenerator.generateJsonFile(folder, 10000, 500, new CNCMachineFactory());
            System.out.println("Test data generating complete");
        }
        Input input = InputResolver.resolve(args);
        JsonStatisticsParser jsonStatisticsParser = new JsonStatisticsParser(input.folder(), input.parameter(), 4);
        System.out.println("JSON statistics parsing...");
        var globalStatistics = Utilities.sortByValue(jsonStatisticsParser.parse());
        System.out.println("JSON statistics parsing complete");
        System.out.println("JSON statistics writing...");
        StatisticsXmlWriter.write(globalStatistics, input.parameter());
        System.out.println("JSON statistics writing complete");
    }
}