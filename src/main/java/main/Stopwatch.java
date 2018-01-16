package main;

import java.util.HashMap;
import java.util.Map;

final class Stopwatch {

    private Long start;

    public Map<Integer, Double> timesMap = new HashMap<>();

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