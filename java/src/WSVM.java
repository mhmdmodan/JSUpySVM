import Holders.ClassPair;
import Holders.HyperParam;
import TwoClass.Predictor;
import TwoClass.TwoHull;
import Vectors.MF;
import Vectors.Vector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Master object handling SVM training
 */
public class WSVM {

    private Map<String, List<Vector>> vectorsMap;
    private Map<ClassPair, Predictor> predictorMap;

    private HyperParam params;

    /**
     * Initializes the SVM with initial datapoints
     * @param nums a flattened array of every training point
     * @param dim dimension of the feature space
     * @param labels labels for each point
     * @param s array of weights for each point
     */
    public WSVM(double[] nums, int dim, String[] labels, double[] s) {

        params = new HyperParam();
        vectorsMap = new ConcurrentHashMap<>();
        predictorMap = new ConcurrentHashMap<>();

        // Initialize a list of vectors for each label
        for (String str:labels) {
            vectorsMap.putIfAbsent(str, new LinkedList<>());
        }
        parseVectors(nums, dim, labels, s);
    }

    public WSVM(double[] nums, int dim, String[] labels) {
        this(nums, dim, labels, MF.fillS(labels.length));
    }

    public HyperParam getParams() {
        return params;
    }

    /**
     * Takes a flattened array of points and converts
     * it into a list of Vector objects
     * @param nums a flattened array of every training point
     * @param dim dimension of the feature space
     * @param labels labels for each point
     * @param s array of weights for each point
     */
    private void parseVectors(double[] nums, int dim, String[] labels, double[] s) {
        int numVec = nums.length/dim;

        int curVec;
        int curNums = 0;

        // Iterate along nums, creating a vectors of length dim
        while (curNums < nums.length) {
            curVec = Math.floorDiv(curNums, dim);
            Vector thisVec = new Vector(dim);
            for (int i=0; i<dim; i++) {
                thisVec.set(i, nums[curNums]);
                curNums++;
            }
            thisVec.setS(s[curVec]);
            vectorsMap.get(labels[curVec]).add(thisVec);
        }
    }

    /**
     * Pairs up each class and trains a TwoHull for each pair
     * @return a master predictor object which holds the predictor for
     * each pair
     */
    public MasterPredictor train() {
        params.createLambdas();
        try {
            findMu();
            // Pair up each class
            Set<ClassPair> pairs = ClassPair.pairUp(vectorsMap.keySet());
            //Train in parallel
            new ArrayList<>(pairs)
                    .stream()
                    .parallel()
                    .forEach(this::trainPair);
        } catch (Exception ex) {
            // Allows for debugging within R
            ex.printStackTrace(new PrintStream(System.out));
        }
        // After each trainPair() call populates predictorMap,
        // create a master predictor object from the map
        return new MasterPredictor(predictorMap);
    }

    /**
     * Trains a single class pair by creating a TwoHull.
     * Puts the resulting predictor into the map of class to predictors
     * @param pair the ClassPair to train
     */
    private void trainPair(ClassPair pair) {
        if (!predictorMap.containsKey(pair)) {
            List<Vector> aVec = vectorsMap.get(pair.getX());
            List<Vector> bVec = vectorsMap.get(pair.getY());

            TwoHull curTwoHull = new TwoHull(aVec.toArray(new Vector[aVec.size()]),
                    bVec.toArray(new Vector[bVec.size()]), pair.getX(), pair.getY(), params);
            Predictor curPred = curTwoHull.runAlgo();
            predictorMap.put(pair, curPred);
        }
    }

    /**
     * If mu has already been set, by a user, use it. Else,
     * find mu based on 1/.9k where k = sum of weights in the
     * smallest by weight class
     */
    private void findMu() {
        if (params.getMu() > 0) return; //ie Mu has been set by user

        double curSum = 0;
        double minSum = 1e10;
        for (Map.Entry<String, List<Vector>> entry:vectorsMap.entrySet()) {
            curSum = entry.getValue()
                    .parallelStream()
                    .mapToDouble(Vector::getS)
                    .sum();
            if (curSum < minSum) {
                minSum = curSum;
            }
        }
        if (minSum == 1e10) {
            minSum = 1/.9;
        }
        params.setMu(1/(0.9*minSum));
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream reader = new FileInputStream("data//poop.txt");
        Scanner sc = new Scanner(reader);
        double[] pts = new double[334];
        int i = 0;
        while (sc.hasNext()) {
            pts[i] = sc.nextDouble();
            i++;
        }

        reader = new FileInputStream("data//poopClass.txt");
        sc = new Scanner(reader);
        String[] classes = new String[167];
        i = 0;
        while (sc.hasNext()) {
            classes[i] = sc.next();
            i++;
        }

        long before = System.currentTimeMillis();
        WSVM test = new WSVM(pts, 2, classes);
        test.getParams().setMu(1);
        test.getParams().setKernel("monomial");
        test.getParams().setQ(2);
        MasterPredictor testPred = test.train();
        long after = System.currentTimeMillis();
        System.out.println((after-before));
        String result = testPred.predict(new double[] {0.05143501,-0.04338526});
        System.out.println(result);
    }
}
