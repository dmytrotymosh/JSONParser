package org.example;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.*;

public class JsonFileGenerator {
    public static <T> void generateJsonFile(
            final Path folder,
            int objectsPerFile,
            int filesNumber,
            EntityFactory<T> entityFactory) throws IOException {
        if (Files.notExists(folder)) {
            Files.createDirectory(folder);
        }
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (int i = 0; i < filesNumber; i++) {
            Path filePath = folder.resolve("part" + i + ".json");
            executor.submit(() -> {
                try {
                    writeJsonFile(filePath, objectsPerFile, entityFactory);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static <T> void writeJsonFile(
            Path filePath,
            int objectsPerFile,
            EntityFactory<T> entityFactory) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonFactory jsonFactory = new JsonFactory();
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8);
             JsonGenerator jsonGenerator = jsonFactory.createGenerator(bufferedWriter)) {
            jsonGenerator.setCodec(mapper);
            jsonGenerator.writeStartArray();
            for (int i = 0; i < objectsPerFile; i++) {
                T entity = entityFactory.create();
                jsonGenerator.writeObject(entity);
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.flush();
        }
    }
}
