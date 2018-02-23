package Holders;

public class HyperParam {
    public static final double epDef = 1e-2;
    public static final double nonSepDef = 1e-5;
    public static final double muDef = -1;

    private double ep;
    private double nonSep;
    private double mu;

    public HyperParam() {
        this.ep = epDef;
        this.nonSep = nonSepDef;
        this.mu = muDef;
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
}
