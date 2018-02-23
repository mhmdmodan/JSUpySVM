package Vectors;

import Exceptions.VectorSizeException;
import Sum.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class MF {

    public static final double ep = 1e-10;

    public static double clamp(double expr, double minim, double maxim) {
        if (expr < minim) {
            return(minim);
        } else if (expr > maxim) {
            return(maxim);
        } else {
            return(expr);
        }
    }

    public static void lengthCheck(Vector dis, Vector that, String operation) {
        if(dis.dim() != that.dim()) {
            throw new VectorSizeException(dis, that, operation);
        }
    }

    public static void lengthCheck(Vector dis, Vector that) {
        lengthCheck(dis, that, "");
    }

    public static Vector normalize(Vector x) {
        return x.subtract(x.getMin()).div(x.getMax() - x.getMin());
    }

    public static boolean eq(double a, double b) {
        return Math.abs(a-b) < ep;
    }

    public static boolean geq(double a, double b) {
        return(a > b);
    }

    public static boolean leq(double a, double b) {
        return(a < b);
    }

    public static double min(double a, double b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }

    public static double kern(Vector a, Vector b) {
        return Math.pow(a.mult(b), 2);
    }

    public static double summ(I op, int end) {
        double toReturn = 0;
        for (int i=0; i<end; i++) {
            toReturn += op.function(i);
        }
        return toReturn;
    }

    public static Vector summ(IV op, int end, int dim) {
        Vector toReturn = new Vector(dim);
        for (int i=0; i<end; i++) {
            toReturn.addRep(op.function(i));
        }
        return toReturn;
    }

    public static double dSumm(IJ op, int iEnd, int jEnd) {
        double iSum = 0;
        double jSum = 0;
        for (int i=0; i<iEnd; i++) {
            jSum = 0;
            for (int j=0; j<jEnd; j++) {
                jSum += op.function(i,j);
            }
            iSum += jSum;
        }
        return iSum;
    }

    public static Vector dSumm(IJV op, int iEnd, int jEnd, int dim) {
        Vector iSum = new Vector(dim);
        Vector jSum = new Vector(dim);
        for (int i=0; i<iEnd; i++) {
            jSum.set(new double[dim]);
            for (int j=0; j<jEnd; j++) {
                jSum.addRep(op.function(i,j));
            }
            iSum.addRep(jSum);
        }
        return iSum;
    }

//    /**
//     * Parses an array of doubles into an array of vectors
//     * with dimension dim. Assigns each vector weight s[i]
//     *
//     * @param nums array of doubles
//     * @param dim dimension for each vector
//     * @param s array of weights for each vector
//     * @return array of parsed vectors
//     */
//    public static Vector[] parseVectors(double[] nums, int dim, String labels, double[] s) {
//        int numVec = nums.length/dim;
//        Vector[] allVecs = new Vector[numVec];
//
//        int curVec;
//        int curNums = 0;
//        while (curNums < nums.length) {
//            curVec = Math.floorDiv(curNums, dim);
//            allVecs[curVec] = new Vector(dim);
//            for (int i=0; i<dim; i++) {
//                allVecs[curVec].set(i, nums[curNums]);
//                curNums++;
//            }
//            allVecs[curVec].setS(s[curVec]);
//        }
//        return allVecs;
//    }
//
//    public static Vector[] parseVectors(double[] nums, int dim) {
//        return parseVectors(nums, dim, fillS(nums.length, dim));
//    }

    private static double[] fillS(int length, int dim) {
        double[] toReturn = new double[length/dim];
        Arrays.fill(toReturn, 1.0);
        return toReturn;
    }

//    public static double[] parseVec(String filename) throws FileNotFoundException {
//        InputStream reader = new FileInputStream(filename);
//        Scanner sc = new Scanner(reader);
//
//    }
}
