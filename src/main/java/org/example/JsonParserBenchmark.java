package org.example;

import org.openjdk.jmh.annotations.*;
import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
public class JsonParserBenchmark {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        @Param({"1", "2", "4", "8"})
        public int threads;
        File folder;
        String parameter;
        JsonStatisticsParser parser;
        @Setup(Level.Trial)
        public void setup() {
            folder = new File("./data");
            parameter = "maker";
            parser = new JsonStatisticsParser(folder, parameter, threads);
        }
    }
    @Benchmark
    public Map<String, Integer> benchmarkParse(BenchmarkState state) {
        return state.parser.parse();
    }
}

