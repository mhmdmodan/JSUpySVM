package NegPosLists;

import Exceptions.ArrayLengthException;

import java.util.Arrays;

public class IntNPList {
    private int[] obs;
    private int length;
    private int negEnd;
    private int[] whichClass;

    public IntNPList(int[] unorderedObs, int[] unorderedClass) {
        if (unorderedObs.length != unorderedClass.length) {
            throw new ArrayLengthException();
        }
        this.length = unorderedObs.length;

        int curNeg = 0;
        int curPos = length-1;
        obs = Arrays.copyOf(unorderedObs,length);
        for (int i=0; i<length; i++) {
            if (unorderedClass[i] < 0) {
                obs[curNeg] = unorderedObs[i];
                curNeg++;
            } else {
                obs[curPos] = unorderedObs[i];
                curPos--;
            }
        }

        negEnd = curNeg+1;
        makeWeights();
    }

    public IntNPList(int[] obs, int negEnd) {
        this.obs = obs;
        this.negEnd = negEnd;
        this.length = obs.length;
        makeWeights();
    }

    private void makeWeights() {
        whichClass = new int[length];
        for (int i=0; i<negEnd; i++) {
            whichClass[i] = -1;
        }
        for (int i=negEnd; i<length; i++) {
            whichClass[i] = 1;
        }
    }

    public int getNegEnd() {
        return negEnd;
    }

    public int getClass(int i) {
        return whichClass[i];
    }

    public int[] getNeg() {
        return Arrays.copyOfRange(obs, 0, negEnd);
    }

    public int getNeg(int i) {
        if (i >= negEnd) {
            throw new ArrayIndexOutOfBoundsException("Not in negative rage!");
        }
        return obs[i];
    }

    public int[] getPos() {
        return Arrays.copyOfRange(obs, negEnd, length);
    }

    public int getPos(int i) {
        return obs[i + negEnd];
    }

    public int getAll(int i) {
        return obs[i];
    }

    public int[] getAll() {
        return obs;
    }

    public void setAll(int i, int value) {
        obs[i] = value;
    }

    public void setAll(int[] newObs) {
        obs = newObs;
    }

    public void setNeg(int i, int value) {
        if (i >= negEnd) {
            throw new ArrayIndexOutOfBoundsException("Not in negative rage!");
        }
        obs[i] = value;
    }

    public void setPos(int i, int value) {
        obs[i+negEnd] = value;
    }

    public int length() {
        return length;
    }
}
