package TwoClass;

import Vectors.*;
import Sum.*;

/**
 * A single class for a given SVM TwoHull. Contains all points
 * of its class and includes methods for finding hull vertices
 * and hull centers.
 */
public class SingleHull {
    private Vector[] pointList;
    private double mu;
    private int dim;
    private int length;
    private int whichClass;

    // weights for the current p vector
    private double[] PWt;
    // weights for the current v vector
    private double[] VWt;
    // weights for the f cache
    private double[] cache;

    private String label;

    /**
     * Initialize the single class object
     * @param pointList an array of vectors corresponding to training points for the class
     * @param mu reduction factor
     * @param label label for this SingleHull's class
     * @param whichClass positive or negative class?
     */
    public SingleHull(Vector[] pointList, double mu, String label, int whichClass) {
        this.label = label;
        this.mu = mu;
        this.pointList = pointList;
        this.length = pointList.length;
        this.dim = pointList[0].dim();
        this.whichClass = whichClass;

        this.cache = new double[length];
        this.PWt = new double[length];
        this.VWt = new double[length];
    }

    /**
     * Find the weighted center of this class
     * @return weighted center as a vector object
     */
    public Vector getCenter() {
        double totalS = 0;
        for (Vector vec:pointList) {
            totalS += vec.getS();
        }
        double[] newS = new double[length];
        for (int i=0; i<length; i++) {
            newS[i] = get(i).getS()/totalS;
        }
        IV fun = (int i) -> get(i).mult(newS[i]);
        return MF.summ(fun, length, dim);
    }

    /**
     * Find the index of the max vector dot product in some direction n.
     * This is for the initial point finding so no kernel products
     * @param n direction to find a point in
     * @param a convex hull weights
     * @return index of the maximum dot product vertex
     */
    private int getMaxIndex(Vector n, double[] a) {
        int maxInd = -1;
        double maxVal = -Double.MAX_VALUE;
        for (int i=0; i<length; i++) {
            if (MF.eq(a[i], 0)) {
                double curVal = n.mult(get(i));
                if (MF.geq(curVal, maxVal)) {
                    maxInd = i;
                    maxVal = curVal;
                }
            }
        }
        return maxInd;
    }

    /**
     * Find the index of the vector with the max dot product in the
     * direction of -w.
     * @param a convex hull weights
     * @return index of the maximum dot product vertex
     */
    private int getMaxIndex(double[] a) {
        int maxInd = -1;
        double maxVal = -Double.MAX_VALUE;
        for (int i=0; i<length; i++) {
            if (MF.eq(a[i], 0)) {
                double curVal = -1 * whichClass * cache[i];
                if (MF.geq(curVal, maxVal)) {
                    maxInd = i;
                    maxVal = curVal;
                }
            }
        }
        return maxInd;
    }

    /**
     * Method to find a point on the convex hull of SingleHull
     * @param n vector for which direction to look in. Can be null when
     *          init is true.
     * @param init boolean if this is finding the initial vector or not.
     *             Determines if we'll use the f cache or not, ie the vector n
     *             would be -w.
     * @return an array of doubles corresponding to convex hull weights. These
     * are a in the equation of a convex hull.
     */
    public double[] findVertex(Vector n, boolean init) {
        double[] a = new double[length];
        double total = 0;
        int i = 0;
        while (!MF.eq(total, 1)) {
            if (init) {
                //for initialization
                i = getMaxIndex(n, a);
            } else {
                i = getMaxIndex(a);
            }

            a[i] = Double.min(get(i).getS()*mu, 1-total);
            total += a[i];
        }
        return a;
    }

    public Vector get(int i) {
        return pointList[i];
    }

    public int dim() {
        return dim;
    }

    public int length() {
        return length;
    }

    public int getWhichClass() {
        return whichClass;
    }

    public double PWt(int i) {
        return PWt[i];
    }

    public double VWt(int i) {
        return VWt[i];
    }

    public double cache(int i) {
        return cache[i];
    }

    public void setPWt(int i, double value) {
        this.PWt[i] = value;
    }

    public void setPWt(double [] vals) {
        this.PWt = vals;
    }

    public void setVWt(int i, double value) {
        this.VWt[i] = value;
    }

    public void setVWt(double [] vals) {
        this.VWt = vals;
    }

    public void setCache(int i, double value) {
        this.cache[i] = value;
    }

    public String getLabel() {
        return label;
    }
}
