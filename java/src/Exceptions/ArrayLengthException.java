package Exceptions;

public class ArrayLengthException extends RuntimeException {
    public ArrayLengthException() {
        super("Array lengths not equal!");
    }
}
