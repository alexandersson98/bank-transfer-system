package se.chasacademy.databaser.banktransfer.exception;

public class InsufficientFundsException extends ValidationException {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
