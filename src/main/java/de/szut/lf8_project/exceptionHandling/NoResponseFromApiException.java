package de.szut.lf8_project.exceptionHandling;

public class NoResponseFromApiException extends RuntimeException {
    
    public NoResponseFromApiException(String message) {
        super(message);
    }
}
