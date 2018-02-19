import NegPosLists.DoubleNPList;
import NegPosLists.IntNPList;
import NegPosLists.NegPosList;
import NegPosLists.PointList;
import Vectors.MF;
import Vectors.Vector;

public class WRCH {
    private int negEnd;
    private PointList ptsList;
    private DoubleNPList allWt;
    private DoubleNPList allVWt;
    private DoubleNPList fCache;

    private double rchFactor;
    private double ep;
    private double nonSep;
    private int numPt;
    private int numNeg;
    private int numPos;

    public WRCH(double[] nums, int dim, double[] s, int[] classes,
                double rchFactor, double ep, double nonSep) {
        this.rchFactor = rchFactor;
        this.ep = ep;
        this.nonSep = nonSep;

        this.ptsList = new PointList(nums, dim, s, classes);
        this.numPt = ptsList.length();
        this.numNeg = ptsList.getNegEnd();
        this.numPos = numPt - ptsList.getNegEnd();
    }

    private DoubleNPList findVertices(Vector direction) {
        double[] a = new double[numPt];
        double total = 0;
        int maxInd = 0;

        while (!MF.eq(total, 1)) {
            double maxVal = 0;
            for (int i=0; i<negEnd; i++) {
                if (a[i] == 0) {
                    double curVal = direction.mult(ptsList.getNeg(i));
                    if (MF.geq(curVal, maxVal)) {
                        maxInd = i;
                        maxVal = curVal;
                    }
                }
            }


        }

        return null;
    }

}