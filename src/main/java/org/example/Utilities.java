package org.example;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Utilities {
    public static LinkedHashMap sortByValue(Map<String, Integer> unsortedMap) {
        LinkedHashMap<String, Integer> sortedMap = unsortedMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        return sortedMap;
    }
    public static Map<String, Integer> calculateStatistics(List<Future<Map<String, Integer>>> futures) {
        Map<String, Integer> globalStatistics = new HashMap<>();
        for (Future<Map<String, Integer>> future : futures) {
            try {
                Map<String, Integer> localStatistics = future.get();
                localStatistics.forEach((K, V) -> globalStatistics.merge(K, V, Integer::sum));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return globalStatistics;
    }
}
