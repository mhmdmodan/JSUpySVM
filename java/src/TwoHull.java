import Exceptions.ArrayLengthException;
import Exceptions.NonSepException;
import Sum.I;
import Sum.IJ;
import Vectors.MF;
import Vectors.Vector;

import java.util.Arrays;

import static Vectors.MF.kern;
import static Vectors.MF.parseVectors;

public class TwoHull {
    private SingleHull neg;
    private SingleHull pos;
    private double ep;
    private double nonSep;
    private int numLoops;

    public static final double epDef = 1e-2;
    public static final double nonSepDef = 1e-5;
    public static final double muDef = 1;

    public TwoHull(double[] nums, int dim, double[] s, int[] whichClass) {
        ep = epDef;
        nonSep = nonSepDef;

        Vector[] allVecs = parseVectors(nums, dim, s);
        createHulls(allVecs, whichClass);
    }

    public TwoHull(double[] nums, int dim, int[] whichClass) {
        ep = epDef;
        nonSep = nonSepDef;

        Vector[] allVecs = parseVectors(nums, dim);
        createHulls(allVecs, whichClass);
    }

    /**
     * Given an array of vectors and array of classes,
     * places vectors in appropriate positive and
     * negative arrays, and creates new SingleHull
     * objects with these vectors.
     *
     * @param unorderedObs unordered array of vectors
     * @param unorderedClass corresponding array of classes
     */
    private void createHulls(Vector[] unorderedObs, int[] unorderedClass) {
        if (unorderedObs.length != unorderedClass.length) {
            throw new ArrayLengthException();
        }

        int curNeg = 0;
        int curPos = 0;

        Vector[] negVec = new Vector[unorderedObs.length];
        Vector[] posVec = new Vector[unorderedObs.length];

        for (int i=0; i<unorderedObs.length; i++) {
            if (unorderedClass[i] <0) {
                negVec[curNeg] = unorderedObs[i];
                negVec[curNeg].setWhichClass(-1);
                curNeg++;
            } else {
                posVec[curPos] = unorderedObs[i];
                posVec[curPos].setWhichClass(1);
                curPos++;
            }
        }

        negVec = Arrays.copyOf(negVec, curNeg);
        posVec = Arrays.copyOf(posVec, curPos);

        neg = new SingleHull(negVec, muDef);
        pos = new SingleHull(posVec, muDef);
    }

    //<editor-fold desc="Getters/Setters">
    public void setMu(int negMu, int posMu) {
        neg.setMu(negMu);
        pos.setMu(posMu);
    }

    public void setMu(int bothMu) {
        setMu(bothMu, bothMu);
    }

    public void setEp(double ep) {
        this.ep = ep;
    }

    public void setNonSep(double nonSep) {
        this.nonSep = nonSep;
    }

    public SingleHull getNeg() {
        return neg;
    }

    public SingleHull getPos() {
        return pos;
    }

    private boolean b(int i) {
        return i < neg.length();
    }

    public int dim() {
        return pos.dim();
    }

    public Vector allGet(int i) {
        return b(i) ? neg.get(i) : pos.get(i-neg.length());
    }

    public int allLength() {
        return neg.length() + pos.length();
    }

    public int getWhichClass(int i) {
        return b(i) ? -1 : 1;
    }

    public double allPWt(int i) {
        return b(i) ? neg.PWt(i) : pos.PWt(i-neg.length());
    }

    public void setAllPWt(int i, double value) {
        if (b(i)) {
            neg.setPWt(i, value);
        } else {
            pos.setPWt(i-neg.length(), value);
        }
    }

    private double allVWt(int i) {
        return b(i) ? neg.VWt(i) : pos.VWt(i-neg.length());
    }

    public void setAllVWt(int i, double value) {
        if (b(i)) {
            neg.setVWt(i, value);
        } else {
            pos.setVWt(i-neg.length(), value);
        }
    }

    private double allCache(int i) {
        return b(i) ? neg.cache(i) : pos.cache(i-neg.length());
    }

    private void setCache(int i, double value) {
        if (b(i)) {
            neg.setCache(i, value);
        } else {
            pos.setCache(i-neg.length(), value);
        }
    }

    public int getNumLoops() {
        return numLoops;
    }

    //</editor-fold>

    private void buildCache() {
        double curSum = 0;
        for (int i = 0; i< allLength(); i++) {
            curSum = 0;
            for (int k=0; k<allLength(); k++) {
                curSum += allPWt(k) * getWhichClass(k) *
                        kern(allGet(k), allGet(i));
            }
            setCache(i, curSum);
        }
    }

    private void setupAlg() {
        Vector centerPos = pos.getCenter();
        Vector centerNeg = neg.getCenter();

        pos.setPWt(pos.findVertex(centerNeg.subtract(centerPos), true));
        neg.setPWt(neg.findVertex(centerPos.subtract(centerNeg), true));

        numLoops = 0;
    }

    public Predictor runAlgo() {
        setupAlg();

        //<editor-fold desc="Shared values">
        I fun1 = (int i) -> (Math.max(getWhichClass(i),0) * allVWt(i) +
                Math.min(getWhichClass(i), 0) * allPWt(i)) * allCache(i);

        I fun2 = (int i) -> (Math.max(getWhichClass(i),0) * allPWt(i) +
                Math.min(getWhichClass(i), 0) * allVWt(i)) * allCache(i);

        I fun3 = (int i) -> allPWt(i) * getWhichClass(i) * allCache(i);
        //</editor-fold>

        //<editor-fold desc="Positive case">
        I posNumer = (int i) -> (pos.PWt(i) - pos.VWt(i)) * pos.cache(i);

        IJ posDenom = (int i, int j) -> (pos.PWt(i) - pos.VWt(i)) *
                (pos.PWt(j) - pos.VWt(j)) * kern(pos.get(i), pos.get(j));
        //</editor-fold>

        //<editor-fold desc="Negative case">
        I negNumer = (int i) -> (neg.PWt(i) - neg.VWt(i)) * neg.cache(i);

        IJ negDenom = (int i, int j) -> (neg.PWt(i) - neg.VWt(i)) *
                (neg.PWt(j) - neg.VWt(j)) * kern(neg.get(i), neg.get(j));
        //</editor-fold>
        numLoops = 0;
        while (true) {
            numLoops++;
            buildCache();

            pos.setVWt(pos.findVertex(null, false));
            neg.setVWt(neg.findVertex(null, false));

            double w0vPos_pNeg = MF.summ(fun1, allLength());
            double w0pPos_vNeg = MF.summ(fun2, allLength());
            double w0w = MF.summ(fun3, allLength());

            if (Math.sqrt(w0w) < nonSep) {
                throw new NonSepException();
            }

            if (w0pPos_vNeg > w0vPos_pNeg) {
                if (1 - w0vPos_pNeg / w0w < ep) break;

                double numerator = MF.summ(posNumer, pos.length());
                double denominator = MF.dSumm(posDenom, pos.length(), pos.length());

                double q = MF.clamp(numerator / denominator, 0 ,1);

                for (int i=0; i<pos.length(); i++) {
                    pos.setPWt(i, (1-q) * pos.PWt(i) + q * pos.VWt(i));
                }
            } else {
                if (1 - w0pPos_vNeg / w0w < ep) break;

                double numerator = MF.summ(negNumer, neg.length());
                double denominator = MF.dSumm(negDenom, neg.length(), neg.length());

                double q = MF.clamp(-numerator / denominator, 0 ,1);

                for (int i=0; i<neg.length(); i++) {
                    neg.setPWt(i, (1-q) * neg.PWt(i) + q * neg.VWt(i));
                }
            }
        }
        return new Predictor(this);
    }

    public static void main(String[] args) {

    }
}