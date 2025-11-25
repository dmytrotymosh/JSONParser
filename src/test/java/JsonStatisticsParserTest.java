import org.example.JsonStatisticsParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonStatisticsParserTest {
    @Test
    void testSingleValues() throws Exception {
        String json = "[\n" +
                "  {\"maker\":\"Okuma\"},\n" +
                "  {\"maker\":\"Okuma\"},\n" +
                "  {\"maker\":\"Mazak\"}\n" +
                "]";
        JsonStatisticsParser parser = new JsonStatisticsParser(null, "maker", 4);
        ByteArrayInputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> result = parser.parseFile(input);
        assertEquals(2, result.size());
        assertEquals(2, result.get("Okuma"));
        assertEquals(1, result.get("Mazak"));
    }
    @Test
    void testMultipleValuesWithSeparators() throws Exception {
        String json = "[\n" +
                "  {\"commandLanguage\":\"Mazatrol#Heidenhain\"},\n" +
                "  {\"commandLanguage\":\"OSP;Fanuc|Siemens\"}\n" +
                "]";
        JsonStatisticsParser parser = new JsonStatisticsParser(null, "commandLanguage", 4);
        ByteArrayInputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> result = parser.parseFile(input);
        assertEquals(5, result.size());
        assertEquals(1, result.get("Mazatrol"));
        assertEquals(1, result.get("Heidenhain"));
        assertEquals(1, result.get("OSP"));
        assertEquals(1, result.get("Fanuc"));
        assertEquals(1, result.get("Siemens"));
    }
    @Test
    void testEmptyOrMissingField() throws Exception {
        String json = "[\n" +
                "  {\"maker\":\"Okuma\"},\n" +
                "  {\"model\":\"VTC-530C\"}\n" +
                "]";
        JsonStatisticsParser parser = new JsonStatisticsParser(null, "commandLanguage", 4);
        ByteArrayInputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> result = parser.parseFile(input);
        assertTrue(result.isEmpty());
    }
    @Test
    void testWhitespaceAndExtraSeparators() throws Exception {
        String json = "[\n" +
                "  {\"commandLanguage\":\"Mazatrol , Heidenhain ; OSP / Fanuc\"}\n" +
                "]";
        JsonStatisticsParser parser = new JsonStatisticsParser(null, "commandLanguage", 4);
        ByteArrayInputStream input = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> result = parser.parseFile(input);
        assertEquals(4, result.size());
        assertEquals(1, result.get("Mazatrol"));
        assertEquals(1, result.get("Heidenhain"));
        assertEquals(1, result.get("OSP"));
        assertEquals(1, result.get("Fanuc"));
    }
}
