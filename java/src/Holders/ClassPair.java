package Holders;

import java.util.HashSet;
import java.util.Set;

/**
 * A class holding two strings. Order doesn't matter.
 */
public class ClassPair {

    private String x;
    private String y;

    public ClassPair(String x, String y) {
        this.x = x;
        this.y = y;
    }

    /**
     * A static function which takes a set of strings
     * and pairs them up in in ever combo into a set of ClassPairs
     * @param labels labels to pair up
     * @return a set of ClassPairs
     */
    public static Set<ClassPair> pairUp(Set<String> labels) {
        Set<ClassPair> set = new HashSet<>();
        for (String str1:labels) {
            for (String str2:labels) {
                if (!str1.equals(str2)) {
                    set.add(new ClassPair(str1, str2));
                }
            }
        }
        return set;
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
