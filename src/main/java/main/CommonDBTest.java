package main;

import java.util.HashMap;
import java.util.Map;

public class CommonDBTest {
    public static void main(String[] args) {
        try {
            System.out.println("#################################################");
            System.out.println("################# GraphDB tests #################");
            System.out.println("#################################################");
            GraphDBBenchmarkTest.runTest();
            System.out.println("###############################################");
            System.out.println("################# RDF4J tests #################");
            System.out.println("###############################################");
            RDF4JDBBenchmarkTest.runTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class Stopwatch {

        private Long start;

        private Map<Integer, Double> timesMap = new HashMap<>();

        public void start() {
            start = System.currentTimeMillis();
        }

        public void stop(int queryId) {
            long now = System.currentTimeMillis();
            double estimatedTime = (now - start);
            timesMap.put(queryId, estimatedTime);
            System.out.println("Query nr: " + queryId + " took: " + estimatedTime);
        }

        public void stopForInsert() {
            long now = System.currentTimeMillis();
            double estimatedTime = (now - start);
            System.out.println("Insert took: " + estimatedTime + " milliseconds.");
        }
    }
}
