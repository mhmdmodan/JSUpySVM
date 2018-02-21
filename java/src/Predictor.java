import Vectors.Vector;

import java.util.Arrays;
import java.util.stream.Stream;

public class Predictor {

    private TwoHull parent;

    private double[] allPWt;
    private int[] whichClass;
    private Vector[] allPts;

    private double bisect;

    public Predictor(TwoHull parent) {
        this.parent = parent;
        this.allPWt = createPWt();
        this.whichClass = createClass();
        this.allPts = createAllPts();
    }

    /**
     * Creates a new array of point weights from parent
     * hulls.
     * @return array of point weights
     */
    private double[] createPWt() {
        double[] allPWt = new double[parent.allLength()];

        for (int i=0; i<parent.allLength(); i++) {
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
        int[] whichClass = new int[parent.allLength()];

        for (int i=0; i<parent.allLength(); i++) {
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
        Vector[] allPts = new Vector[parent.allLength()];

        for (int i=0; i<parent.allLength(); i++) {
            allPts[i] = parent.allGet(i);
        }

        return allPts;
    }
}
