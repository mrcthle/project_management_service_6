package de.szut.lf8_project.exceptionHandling;

public class EmployeeNotAvailableException extends RuntimeException {
    
    public EmployeeNotAvailableException(String message) {
        super(message);
    }
}
