package Sum;

import Vectors.Vector;

@FunctionalInterface
public interface Kernel {
    public double kern(Vector a, Vector b);
}
