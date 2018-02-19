package NegPosLists;

import Vectors.Vector;

public class PointList extends NegPosList<Vector> {
    private int dim;

    public PointList(Vector[] obs, int[] classes) {
        super(obs, classes);
        this.dim = obs[1].dim();
    }

    public PointList(double[] nums, int dim, double[] weights, int[] classes) {
        this(deconstruct(nums,dim,weights), classes);
    }

    private static Vector[] deconstruct(double[] nums, int dim, double[] weights) {
        int numVec = nums.length/dim;
        Vector[] toReturn = new Vector[numVec];

        int curVec;
        int curNums = 0;
        while (curNums < nums.length) {
//            curVec = Math.floorDiv(curNums, dim);
//            toReturn[curVec] = new Vector(dim);
//            for (int i=0; i<dim; i++) {
//                toReturn[curVec].set(i, nums[curNums]);
//                curNums++;
//            }
//            toReturn[curVec].setS(weights[curVec]);
        }
        return toReturn;
    }

    public int getDim() {
        return dim;
    }
}
