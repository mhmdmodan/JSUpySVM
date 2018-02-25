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

public class WSVM {

    private Map<String, List<Vector>> vectorsMap;
    private Map<ClassPair, Predictor> predictorMap;

    private HyperParam params;

    public WSVM(double[] nums, int dim, String[] labels, double[] s) {

        params = new HyperParam();
        vectorsMap = new ConcurrentHashMap<>();
        predictorMap = new ConcurrentHashMap<>();

        for (String str:labels) {
            vectorsMap.putIfAbsent(str, new LinkedList<Vector>());
        }
        parseVectors(nums, dim, labels, s);
    }

    public WSVM(double[] nums, int dim, String[] labels) {
        this(nums, dim, labels, MF.fillS(labels.length));
    }

    public HyperParam getParams() {
        return params;
    }

    private void parseVectors(double[] nums, int dim, String[] labels, double[] s) {
        int numVec = nums.length/dim;

        int curVec;
        int curNums = 0;
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

    public MasterPredictor train() {
        try {
            findMu();
            Set<ClassPair> pairs = ClassPair.pairUp(vectorsMap.keySet());
            pairs
                    .parallelStream()
                    .forEach(this::trainPair);
        } catch (Exception ex) {
            ex.printStackTrace(new PrintStream(System.out));
        }
        return new MasterPredictor(predictorMap);
    }

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
        MasterPredictor testPred = test.train();
        long after = System.currentTimeMillis();
        System.out.println((after-before));
        String result = testPred.predict(new double[] {0.05143501,-0.04338526});
        System.out.println(result);
    }
}
