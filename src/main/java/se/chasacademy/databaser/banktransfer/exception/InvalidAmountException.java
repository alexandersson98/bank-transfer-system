package se.chasacademy.databaser.banktransfer.exception;

public class InvalidAmountException extends ValidationException {
    public InvalidAmountException(String message) {
        super(message);
    }
    public InvalidAmountException (String message, Throwable cause){
        super(message, cause);
    }
}
