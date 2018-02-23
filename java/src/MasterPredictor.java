import Holders.ClassPair;
import TwoClass.Predictor;
import Vectors.Vector;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class MasterPredictor {
    private Map<ClassPair, Predictor> predictorMap;

    public MasterPredictor(Map<ClassPair, Predictor> predictorMap) {
        this.predictorMap = predictorMap;
    }

    public String predict(double[] in) {
        Vector inVec = new Vector(in);

        Optional<Map.Entry<String, Long>>  max= predictorMap.entrySet()
                .parallelStream()
                .map(cp -> cp.getValue().predict(inVec))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        return max.get().getKey();
    }
}
