package Exceptions;

import Vectors.Vector;

public class VectorSizeException extends RuntimeException {
    public VectorSizeException(Vector a, Vector b, String operation) {
        super(operation + " sizes not equal! a: " + a.dim() + ", b: " + b.dim());
    }
}