package se.chasacademy.databaser.banktransfer.exception;

public class TechnicalFailureException extends RuntimeException {

    public TechnicalFailureException(String message) {
        super(message);
    }

    public TechnicalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}