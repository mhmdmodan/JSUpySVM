import Vectors.*;
import Sum.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class SingleHull {
    private Vector[] pointList;
    private double mu;
    private int dim;
    private int length;
    private int whichClass;
    private double[] PWt;
    private double[] VWt;
    private double[] cache;

    public SingleHull(Vector[] pointList, double mu) {
        this.mu = mu;
        this.pointList = pointList;
        this.length = pointList.length;
        this.dim = pointList[0].dim();
        this.whichClass = pointList[0].getWhichClass();

        this.cache = new double[length];
        this.PWt = new double[length];
        this.VWt = new double[length];
    }

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
        return MF.summ(fun, dim, dim);
    }

    private int getMaxIndex(Vector n, double[] a) {
        int maxInd = -1;
        double maxVal = 0;
        for (int i=0; i<length; i++) {
            if (a[i] == 0) {
                double curVal = n.mult(get(i));
                if (MF.geq(curVal, maxVal)) {
                    maxInd = i;
                    maxVal = curVal;
                }
            }
        }
        return maxInd;
    }

    private int getMaxIndex(double[] a) {
        int maxInd = -1;
        double maxVal = -1 * whichClass * cache[0];
        for (int i=0; i<length; i++) {
            if (a[i] == 0) {
                double curVal = -1 * whichClass * cache[i];
                if (MF.geq(curVal, maxVal)) {
                    maxInd = i;
                    maxVal = curVal;
                }
            }
        }
        return maxInd;
    }

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

    public void setMu(double mu) {
        this.mu = mu;
    }

    public void printPWt() {
        System.out.println(Arrays.toString(PWt));
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream reader = new FileInputStream("data//poop.txt");
        Scanner sc = new Scanner(reader);
        double[] pts = new double[334];
        int i = 0;
        while (sc.hasNext()) {
            pts[i] = sc.nextDouble();
            i++;
        }

        reader = new FileInputStream("data//poopClass.txt");
        sc = new Scanner(reader);
        int[] classes = new int[167];
        i = 0;
        while (sc.hasNext()) {
            classes[i] = sc.nextInt();
            i++;
        }

        long before = System.currentTimeMillis();
        TwoHull test = new TwoHull(pts, 2, classes);
        test.runAlg();
        long after = System.currentTimeMillis();

        System.out.println(after-before);
        System.out.println(test.getNumLoops());
    }
}
