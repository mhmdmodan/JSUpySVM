package TwoClass;

import Sum.I;
import Sum.IJ;
import Sum.Kernel;
import Vectors.Vector;

import static Vectors.MF.dSumm;
import static Vectors.MF.summ;

public class Predictor {

    private double[] allPWt;
    private int[] whichClass;
    private Vector[] allPts;

    private int length;
    private double bisect;

    private String negLabel;
    private String posLabel;

    private Kernel kf;

    public Predictor(TwoHull parent) {
        this.negLabel = parent.getNeg().getLabel();
        this.posLabel = parent.getPos().getLabel();
        this.kf = parent.getParams().getKf();

        this.length = parent.allLength();
        this.allPWt = createPWt(parent);
        this.whichClass = createClass(parent);
        this.allPts = createAllPts(parent);

        this.bisect = calcBisect();
    }

    /**
     * Creates a new array of point weights from parent
     * hulls.
     * @return array of point weights
     */
    private double[] createPWt(TwoHull parent) {
        double[] allPWt = new double[length];

        for (int i=0; i<length; i++) {
            allPWt[i] = parent.allPWt(i);
        }

        return allPWt;
    }

    /**
     * Creates a new array of class ints from parent
     * hulls. We are doing this rather than use internal
     * Vector class because that can change with other
     * SVMs between other classes
     * @return array of which class
     */
    private int[] createClass(TwoHull parent) {
        int[] whichClass = new int[length];

        for (int i=0; i<length; i++) {
            whichClass[i] = parent.getWhichClass(i);
        }

        return whichClass;
    }

    /**
     * Creates a new array of vectors from parent
     * hulls. Shallow copy is okay here since
     * vector array in TwoClass.SingleHull not mutable
     * @return array of vectors
     */
    private Vector[] createAllPts(TwoHull parent) {
        Vector[] allPts = new Vector[length];

        for (int i=0; i<length; i++) {
            allPts[i] = parent.allGet(i);
        }

        return allPts;
    }

    private double calcBisect() {
        IJ calcB = (int i, int j) -> allPWt[i] * whichClass[i] * allPWt[j] *
                kf.kern(allPts[i], allPts[j]);
        return 0.5*dSumm(calcB, length, length);
    }

    public String predict(Vector inVec) {
        I wCalc = (int i) -> allPWt[i]*whichClass[i]*kf.kern(allPts[i],inVec);
        double wVal = summ(wCalc, length);

        return wVal - bisect > 0 ? posLabel : negLabel;
    }
}
