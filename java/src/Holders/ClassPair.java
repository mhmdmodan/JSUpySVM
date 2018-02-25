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

        if (x.equals(classPair.x) && y.equals(classPair.y)) return true;
        if (x.equals(classPair.y) && y.equals(classPair.x)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return x.hashCode() + y.hashCode();
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
}
