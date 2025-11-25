import org.example.Utilities;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class UtilitiesTest {
    @Test
    void testSortByValueNormalCase() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 2);
        map.put("B", 5);
        map.put("C", 1);
        LinkedHashMap<String, Integer> sorted = Utilities.sortByValue(map);
        Iterator<Map.Entry<String, Integer>> it = sorted.entrySet().iterator();
        Map.Entry<String, Integer> first = it.next();
        Map.Entry<String, Integer> second = it.next();
        Map.Entry<String, Integer> third = it.next();
        assertEquals("B", first.getKey());
        assertEquals(5, first.getValue());
        assertEquals("A", second.getKey());
        assertEquals(2, second.getValue());
        assertEquals("C", third.getKey());
        assertEquals(1, third.getValue());
    }
    @Test
    void testSortByValueWithTies() {
        Map<String, Integer> map = new HashMap<>();
        map.put("X", 3);
        map.put("Y", 5);
        map.put("Z", 3);
        LinkedHashMap<String, Integer> sorted = Utilities.sortByValue(map);
        List<Integer> values = new ArrayList<>(sorted.values());
        assertEquals(Arrays.asList(5, 3, 3), values);
    }
    @Test
    void testSortByValueEmptyMap() {
        Map<String, Integer> map = new HashMap<>();
        LinkedHashMap<String, Integer> sorted = Utilities.sortByValue(map);
        assertTrue(sorted.isEmpty());
    }
    @Test
    void testSortByValueSingleElement() {
        Map<String, Integer> map = Map.of("A", 10);
        LinkedHashMap<String, Integer> sorted = Utilities.sortByValue(map);
        assertEquals(1, sorted.size());
        assertEquals(10, sorted.get("A"));
    }
    @Test
    void testCalculateStatisticsNormalCase() throws Exception {
        Map<String, Integer> map1 = Map.of("A", 2, "B", 3);
        Map<String, Integer> map2 = Map.of("B", 2, "C", 4);
        List<Future<Map<String, Integer>>> futures = Arrays.asList(
                CompletableFuture.completedFuture(map1),
                CompletableFuture.completedFuture(map2)
        );
        Map<String, Integer> result = Utilities.calculateStatistics(futures);
        assertEquals(3, result.size());
        assertEquals(2, result.get("A"));
        assertEquals(5, result.get("B"));
        assertEquals(4, result.get("C"));
    }
    @Test
    void testCalculateStatisticsEmptyList() {
        List<Future<Map<String, Integer>>> futures = Collections.emptyList();
        Map<String, Integer> result = Utilities.calculateStatistics(futures);
        assertTrue(result.isEmpty());
    }
    @Test
    void testCalculateStatisticsSingleFuture() {
        Map<String, Integer> map = Map.of("X", 7, "Y", 1);
        List<Future<Map<String, Integer>>> futures = List.of(CompletableFuture.completedFuture(map));
        Map<String, Integer> result = Utilities.calculateStatistics(futures);
        assertEquals(2, result.size());
        assertEquals(7, result.get("X"));
        assertEquals(1, result.get("Y"));
    }
}

