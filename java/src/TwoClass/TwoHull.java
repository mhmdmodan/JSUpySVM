package TwoClass;

import Exceptions.ArrayLengthException;
import Exceptions.NonSepException;
import Holders.HyperParam;
import Sum.I;
import Sum.IJ;
import Sum.Kernel;
import Vectors.MF;
import Vectors.Vector;

public class TwoHull {
    private SingleHull neg;
    private SingleHull pos;
    private int numLoops;
    private String negLabel;
    private String posLabel;

    private HyperParam params;

    public TwoHull(Vector[] negVec,
                   Vector[] posVec,
                   String negLabel,
                   String posLabel,
                   HyperParam params) {
        this.params = params;
        this.negLabel = negLabel;
        this.posLabel = posLabel;

        neg = new SingleHull(negVec, params.getMu(), negLabel, -1);
        pos = new SingleHull(posVec, params.getMu(), posLabel, 1);
    }

    //<editor-fold desc="Getters/Setters">
    public HyperParam getParams() {
        return params;
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

    private void buildCache(Kernel kf) {
        double curSum = 0;
        for (int i = 0; i< allLength(); i++) {
            curSum = 0;
            for (int k=0; k<allLength(); k++) {
                curSum += allPWt(k) * getWhichClass(k) *
                        kf.kern(allGet(k), allGet(i));
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

        Kernel kf = params.getKf();
        //</editor-fold>

        //<editor-fold desc="Positive case">
        I posNumer = (int i) -> (pos.PWt(i) - pos.VWt(i)) * pos.cache(i);

        IJ posDenom = (int i, int j) -> (pos.PWt(i) - pos.VWt(i)) *
                (pos.PWt(j) - pos.VWt(j)) * kf.kern(pos.get(i), pos.get(j));
        //</editor-fold>

        //<editor-fold desc="Negative case">
        I negNumer = (int i) -> (neg.PWt(i) - neg.VWt(i)) * neg.cache(i);

        IJ negDenom = (int i, int j) -> (neg.PWt(i) - neg.VWt(i)) *
                (neg.PWt(j) - neg.VWt(j)) * kf.kern(neg.get(i), neg.get(j));
        //</editor-fold>
        numLoops = 0;
        while (true) {
            numLoops++;
            buildCache(kf);

            pos.setVWt(pos.findVertex(null, false));
            neg.setVWt(neg.findVertex(null, false));

            double w0vPos_pNeg = MF.summ(fun1, allLength());
            double w0pPos_vNeg = MF.summ(fun2, allLength());
            double w0w = MF.summ(fun3, allLength());

            if (Math.sqrt(w0w) < params.getNonSep()) {
                throw new NonSepException();
            }
            if (numLoops > params.getMaxIts()) {
                System.out.println("MAXIMUM ITERATIONS REACHED: " +
                        params.getMaxIts());
                System.out.println("Training: " + negLabel +
                " vs. " + posLabel);
                System.out.println("1 - w.(Vpos-pNeg)/w.w: " +
                        (1 - w0vPos_pNeg / w0w));
                System.out.println("1 - w.(Ppos-Vneg)/w.w: " +
                        (1 - w0pPos_vNeg / w0w));
                System.out.println("Epsilon: " + params.getEp());
                break;
            }

            if (w0pPos_vNeg > w0vPos_pNeg) {
                if (1 - w0vPos_pNeg / w0w < params.getEp()) break;

                double numerator = MF.summ(posNumer, pos.length());
                double denominator = MF.dSumm(posDenom, pos.length(), pos.length());

                double q = MF.clamp(numerator / denominator, 0 ,1);

                for (int i=0; i<pos.length(); i++) {
                    pos.setPWt(i, (1-q) * pos.PWt(i) + q * pos.VWt(i));
                }
            } else {
                if (1 - w0pPos_vNeg / w0w < params.getEp()) break;

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