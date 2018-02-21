package Exceptions;

public class NonSepException extends RuntimeException {
    public NonSepException() {
        super("Hulls not separable");
    }
}
