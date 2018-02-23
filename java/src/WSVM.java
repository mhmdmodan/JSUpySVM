import Holders.ClassPair;
import Holders.HyperParam;
import TwoClass.Predictor;
import TwoClass.TwoHull;
import Vectors.Vector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class WSVM {

    private Map<String, List<Vector>> vectorsMap;
    private Map<ClassPair, Predictor> predictorMap;

    private HyperParam params;

    public WSVM(double[] nums, int dim, String[] labels, double[] s) {
        params = new HyperParam();
        vectorsMap = new HashMap<>();
        predictorMap = new HashMap<>();

        for (String str:labels) {
            vectorsMap.putIfAbsent(str, new LinkedList<Vector>());
        }
        parseVectors(nums, dim, labels, s);
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
        findMu();
        Set<String> labels = vectorsMap.keySet();
        for (String str1:labels) {
            for (String str2:labels) {
                if (!str1.equals(str2)) {
                    trainPair(str1, str2);
                }
            }
        }
        return new MasterPredictor(predictorMap);
    }

    private void trainPair(String classA, String classB) {
        ClassPair pair = new ClassPair(classA, classB);

        if (!predictorMap.containsKey(pair)) {
            List<Vector> aVec = vectorsMap.get(classA);
            List<Vector> bVec = vectorsMap.get(classB);

            TwoHull curTwoHull = new TwoHull(aVec.toArray(new Vector[0]),
                    bVec.toArray(new Vector[0]), classA, classB, params);
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

        double[] s = new double[167];
        Arrays.fill(s, 1);
        WSVM test = new WSVM(pts, 2, classes, s);
        test.getParams().setMu(1);
        MasterPredictor testPred = test.train();
        String result = testPred.predict(new double[] {0.05143501,-0.04338526});
        System.out.println(result);

//        long before = System.currentTimeMillis();
//        for (int j=0; j<2000; j++) {
////            TwoHull test = new TwoHull(pts, 2, classes, "This", "That");
////            test.runAlgo();
//        }
//        long after = System.currentTimeMillis();
//
//        System.out.println((after-before)/2000);
    }
}
