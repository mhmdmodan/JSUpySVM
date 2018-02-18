import Sum.I;
import Vectors.MF;

public class test {
    public double[] a = new double[] {2,4,6};

    public double summer(I op, int length) {
        double toReturn = 0;
        for (int i=0; i<length; i++) {
            toReturn += op.function(i);
        }
        return toReturn;
    }

    public double sumA() {
        I aInd = (int i) -> a[i]+1;
        return MF.summ(aInd, a.length);
    }

    public static void main(String javalatte[]) {
        // this is lambda expression
        test tester = new test();
        System.out.println(tester.sumA());
    }
}
