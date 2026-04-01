package se.chasacademy.databaser.banktransfer.exception;

public class SameAccountTransferException extends ValidationException {
    public SameAccountTransferException(String message) {
        super(message);
    }
    public SameAccountTransferException(String message, Throwable cause) {super (message, cause); }
}
