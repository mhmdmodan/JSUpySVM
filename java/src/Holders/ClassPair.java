package Holders;

public class ClassPair {

    private String x;
    private String y;

    public ClassPair(String x, String y) {
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassPair classPair = (ClassPair) o;

        if (!x.equals(classPair.x)) return false;
        return y.equals(classPair.y);
    }

    @Override
    public int hashCode() {
        return x.hashCode() + y.hashCode();
    }
}
