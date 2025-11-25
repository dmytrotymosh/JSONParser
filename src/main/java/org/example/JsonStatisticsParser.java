package org.example;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonStatisticsParser {
    private JsonFactory jsonFactory;
    private File folder;
    private String parameter;
    private int threadsNumber;
    public JsonStatisticsParser(File folder, String parameter, int threadsNumber) {
        this.folder = folder;
        this.parameter = parameter;
        this.threadsNumber = threadsNumber;
        this.jsonFactory = new JsonFactory();
    }
    public Map<String, Integer> parse() {
        if (!folder.exists() || !folder.isDirectory()) {
            throw new RuntimeException("Folder " + folder.getName() + " is not a directory");
        }
        List<File> jsonFiles = new ArrayList<>();
        getJsonFiles(folder, jsonFiles);
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();
        for (File file : jsonFiles) {
            futures.add(executor.submit(() -> parseFile(new FileInputStream(file))));
        }
        executor.shutdown();
        return Utilities.calculateStatistics(futures);
    }
    private void getJsonFiles(File folder, List<File> result) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                getJsonFiles(file, result);
            } else {
                if (file.exists() && file.getName().endsWith(".json")) {
                    result.add(file);
                }
            }
        }
    }
    public Map<String, Integer> parseFile(InputStream stream) throws IOException {
        try (JsonParser parser = jsonFactory.createParser(stream)) {
            HashMap<String, Integer> localStatistics = new HashMap<>();
            if (parser.nextToken() == JsonToken.START_ARRAY) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                        String fieldName = parser.getCurrentName();
                        if (parameter.equals(fieldName)) {
                            if (parser.nextToken() == JsonToken.START_ARRAY) {
                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                    localStatistics.merge(parser.getText(), 1, Integer::sum);
                                }
                            } else {
                                String[] values = parser.getText().trim().split("\\s*[,#;|/]+\\s*", -1);
                                for (String value : values) {
                                    localStatistics.merge(value, 1, Integer::sum);
                                }
                            }
                        }
                    }
                }
            }
            return localStatistics;
        }
    }
}
