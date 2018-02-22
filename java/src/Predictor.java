import Sum.I;
import Sum.IJ;
import Vectors.Vector;

import static Vectors.MF.dSumm;
import static Vectors.MF.kern;
import static Vectors.MF.summ;

import java.util.Arrays;

public class Predictor {

    private TwoHull parent;

    private double[] allPWt;
    private int[] whichClass;
    private Vector[] allPts;

    private int length;
    private double bisect;

    public Predictor(TwoHull parent) {
        this.length = parent.allLength();
        this.parent = parent;
        this.allPWt = createPWt();
        this.whichClass = createClass();
        this.allPts = createAllPts();

        this.bisect = calcBisect();
    }

    /**
     * Creates a new array of point weights from parent
     * hulls.
     * @return array of point weights
     */
    private double[] createPWt() {
        double[] allPWt = new double[length];

        for (int i=0; i<length; i++) {
            allPWt[i] = parent.allPWt(i);
        }

        return allPWt;
    }

    /**
     * Creates a new array of class ints from parent
     * hulls.
     * @return array of which class
     */
    private int[] createClass() {
        int[] whichClass = new int[length];

        for (int i=0; i<length; i++) {
            whichClass[i] = parent.getWhichClass(i);
        }

        return whichClass;
    }

    /**
     * Creates a new array of vectors from parent
     * hulls. Shallow copy is okay here since
     * vector array in SingleHull not mutable
     * @return array of vectors
     */
    private Vector[] createAllPts() {
        Vector[] allPts = new Vector[length];

        for (int i=0; i<length; i++) {
            allPts[i] = parent.allGet(i);
        }

        return allPts;
    }

    private double calcBisect() {
        IJ calcB = (int i, int j) -> allPWt[i] * whichClass[i] * allPWt[j] *
                kern(allPts[i], allPts[j]);
        return 0.5*dSumm(calcB, length, length);
    }

    public String predict(double[] in) {
        Vector inVec = new Vector(in);
        I wCalc = (int i) -> allPWt[i]*whichClass[i]*kern(allPts[i],inVec);
        double wVal = summ(wCalc, length);

        return wVal - bisect > 0 ? parent.getPos().getLabel() : parent.getNeg().getLabel();
    }
}
