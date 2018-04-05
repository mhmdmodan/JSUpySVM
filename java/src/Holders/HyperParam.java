package Holders;

import Sum.Kernel;
import Vectors.Vector;

/**
 * A class holding several parameters for the SVM
 */
public class HyperParam {
    public static final double epDef = 1e-2;
    public static final double nonSepDef = 1e-5;
    public static final double muDef = -1;
    public static final int maxItsDef = 2000;

    private double ep;
    private double nonSep;
    private double mu;
    private int maxIts;

    private double q;
    private double gamma;
    private String kernel;
    private Kernel kf;

    /**
     * Initialize the HyperParam with some values
     */
    public HyperParam() {
        this.ep = epDef;
        this.nonSep = nonSepDef;
        this.mu = muDef;
        this.maxIts = maxItsDef;
        this.q = 2;
        this.gamma = 0.01;
    }

    public void setMaxIts(int maxIts) {
        this.maxIts = maxIts;
    }

    public int getMaxIts() {
        return maxIts;
    }

    public double getEp() {
        return ep;
    }

    public void setEp(double ep) {
        this.ep = ep;
    }

    public double getNonSep() {
        return nonSep;
    }

    public void setNonSep(double nonSep) {
        this.nonSep = nonSep;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    /**
     * Create the lambdas to be used for kernel products.
     * This allows a user to choose a kernel for calculations
     */
    public void createLambdas() {
        switch (kernel) {
            case("polynomial"):
                kf = (Vector a, Vector b) -> Math.pow(a.mult(b) + 1, q);
                break;
            case("monomial"):
                kf = (Vector a, Vector b) -> Math.pow(a.mult(b), q);
                break;
            case("linear"):
                kf = Vector::mult;
                break;
            default:
                kf = (Vector a, Vector b) ->
                        Math.exp(-gamma * a.subtract(b).normSq());
                break;
        }
    }

    public Kernel getKf() {
        return kf;
    }
}
