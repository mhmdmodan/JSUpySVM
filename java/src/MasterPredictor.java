import Holders.ClassPair;
import TwoClass.Predictor;
import Vectors.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MasterPredictor {
    private Map<ClassPair, Predictor> predictorMap;
    public static final int maxIts = 5;

    public MasterPredictor(Map<ClassPair, Predictor> predictorMap) {
        this.predictorMap = predictorMap;
    }

    public String predict(double[] in) {
        Vector inVec = new Vector(in);
        Map<ClassPair, String> predictionMap = new ConcurrentHashMap<>();
        predictorMap.entrySet()
                .parallelStream()
                .forEach(entry -> {
                    String label = entry.getValue().predict(inVec);
                    predictionMap.put(entry.getKey(), label);
                });
        return runOffVoting(predictionMap);
    }

    public static String runOffVoting(Map<ClassPair, String> predictionMap) {
        Map<ClassPair, String> currentPredictions = new HashMap<>(predictionMap);
        Set<String> winners = getMode(currentPredictions);
        int its = 0;
        while (winners.size() != 1) {

            if (its >= maxIts) {
                Random rand = new Random();
                int randNum = rand.nextInt(winners.size());
                return winners.toArray(new String[winners.size()])[randNum];
            }

            currentPredictions.clear();
            Set<ClassPair> pairs = ClassPair.pairUp(winners);
            for (ClassPair pair:pairs) {
                currentPredictions.put(pair, predictionMap.get(pair));
            }

            winners = getMode(currentPredictions);
            its++;
        }
        return winners.toArray(new String[winners.size()])[0];
    }

    private static Set<String> getMode(Map<ClassPair, String> currentPredictions) {
        Map<String, Long> counts = currentPredictions.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()));

        Set<String> maxEntries = new HashSet<>();
        long maxCount = 0;
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            if (maxEntries.isEmpty() || entry.getValue() == maxCount) {
                maxEntries.add(entry.getKey());
                maxCount = entry.getValue();
            } else if (entry.getValue() > maxCount) {
                maxEntries.clear();
                maxEntries.add(entry.getKey());
                maxCount = entry.getValue();
            }
        }
        return maxEntries;
    }
}