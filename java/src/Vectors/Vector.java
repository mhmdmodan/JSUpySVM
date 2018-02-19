package Vectors;

import java.util.Arrays;

/**
 * A class to define vectors and their operations.
 * Would have made into an interface to allow for
 * long/int vectors etc but primitives cannot be
 * generic. Same reason why it's not iterable.
 * Using primitives over objects bc faster computation.
 */
public class Vector {

    private double[] vector;
    private int dim;
    private boolean modified;
    private double norm;
    private int[] minMax;
    private double s;
    private int whichClass;

    public Vector(double[] vector) {
        this.vector = vector;
        this.dim = vector.length;
        this.modified = false;
        this.s = 1;
    }

    public Vector(int dim) {
        this.vector = new double[dim];
        this.dim = dim;
        this.modified = false;
        this.s = 1;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getS() {
        return s;
    }

    public int getWhichClass() {
        return whichClass;
    }

    public void setWhichClass(int whichClass) {
        this.whichClass = whichClass;
    }

    /**
     * Get the entire array
     * @return - double[] vector
     */
    public double[] get() {
        return(vector);
    }

    /**
     * Get value of vector at specific index
     * @param i
     * @return
     */
    public double get(int i) {
        return(vector[i]);
    }

    /**
     * Set value of vector at specific index
     * @param i
     * @param value
     */
    public void set(int i, double value) {
        vector[i] = value;
        modified = true;
    }

    /**
     * Set all values of vector
     * @param newVec
     */
    public void set(double[] newVec) {
        vector = newVec;
        modified = true;
    }

    /**
     * Returns vector size/dim
     * @return
     */
    public int dim() {
        return(dim);
    }

    /**
     * Helper to ensure same size vectors for ops
     * @param that
     * @param operation
     */
    private void checkSize(Vector that, String operation) {
        MF.lengthCheck(this, that, operation);
    }

    /**
     * Add that to this
     * @param that
     * @return new vector
     */
    public Vector add(Vector that) {
        checkSize(that, "Vector addition");

        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i) + that.get(i));
        }
        return(toReturn);
    }

    public void addRep(Vector that) {
        checkSize(that, "Vector addition");

        for (int i = 0; i < dim(); i++) {
            set(i,  get(i) + that.get(i));
        }
    }

    public Vector add(double that) {
        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i)+that);
        }
        return(toReturn);
    }

    /**
     * Subtract that from this
     * @param that
     * @return new vector
     */
    public Vector subtract(Vector that) {
        checkSize(that, "Vector subtraction");

        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i) - that.get(i));
        }
        return(toReturn);
    }

    public Vector subtract(double that) {
        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i)-that);
        }
        return(toReturn);
    }

    /**
     * Inner product of this and that
     * @param that
     * @return
     */
    public double mult(Vector that) {
        checkSize(that, "Inner product");

        double toReturn = 0;
        for (int i = 0; i < dim(); i++) {
            toReturn += get(i)*that.get(i);
        }
        return(toReturn);
    }

    /**
     * Multiply this by scalar
     * @param that
     * @return new vector
     */
    public Vector mult(double that) {
        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i)*that);
        }
        return(toReturn);
    }

    /**
     * Divide this by scalar
      * @param that
     * @return new vector
     */
    public Vector div(double that) {
        Vector toReturn = new Vector(dim);
        for (int i = 0; i < dim(); i++) {
            toReturn.set(i,  get(i)/that);
        }
        return(toReturn);
    }

    /**
     * Calculates norm squared and stores it.
     * @return
     */
    public double normSq() {
        if (norm == 0 || modified) {
            norm = mult(this);
        }
        modified = false;
        return(norm);
    }

    /**
     * Finds the min and max indices, and stores it
     */
    private void findMinMaxInd() {
        if (minMax == null || modified) {
            int curMin = 0;
            int curMax = 0;

            for (int i = 0; i< dim; i++) {
                if (get(i) > get(curMax)) {
                    curMax = i;
                }
                if (get(i) < get(curMin)) {
                    curMin = i;
                }
            }
            minMax = new int[] {curMin, curMax};
            modified = false;
        }
    }

    public double getMin() {
        findMinMaxInd();
        return minMax[0];
    }

    public double getMax() {
        findMinMaxInd();
        return minMax[1];
    }

    @Override
    public String toString() {
        return "Vector{"+ Arrays.toString(vector) +
                ", s=" + s +
                '}';
    }
}
