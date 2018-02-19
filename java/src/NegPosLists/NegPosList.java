package NegPosLists;

import Exceptions.ArrayLengthException;

import java.util.Arrays;

public class NegPosList<E> {
    protected E[] obs;
    protected int length;
    protected int negEnd;
    protected int[] whichClass;

    public NegPosList(E[] unorderedObs, int[] unorderedClass) {
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

    public NegPosList(E[] obs, int negEnd) {
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

    public E[] getNeg() {
        return Arrays.copyOfRange(obs, 0, negEnd);
    }

    public E getNeg(int i) {
        if (i >= negEnd) {
            throw new ArrayIndexOutOfBoundsException("Not in negative rage!");
        }
            return obs[i];
    }

    public E[] getPos() {
        return Arrays.copyOfRange(obs, negEnd, length);
    }

    public E getPos(int i) {
        return obs[i + negEnd];
    }

    public E getAll(int i) {
        return obs[i];
    }

    public E[] getAll() {
        return obs;
    }

    public void setAll(int i, E value) {
        obs[i] = value;
    }

    public void setAll(E[] newObs) {
        obs = newObs;
    }

    public void setNeg(int i, E value) {
        if (i >= negEnd) {
            throw new ArrayIndexOutOfBoundsException("Not in negative rage!");
        }
        obs[i] = value;
    }

    public void setPos(int i, E value) {
        obs[i+negEnd] = value;
    }

    public int length() {
        return length;
    }

    @Override
    public String toString() {
        return "NegPosList{" +
                "obs=" + Arrays.toString(obs) +
                '}';
    }
}
