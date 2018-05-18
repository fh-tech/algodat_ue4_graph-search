package com.fhtech.algue4.errors;

public class DijkstraException extends Exception {

    public DijkstraException() {}
    public DijkstraException(String message) {
        super(message);
    }
    public DijkstraException(String message, Throwable cause) {
        super(message, cause);
    }
    public DijkstraException(Throwable cause) {
        super(cause);
    }

}
