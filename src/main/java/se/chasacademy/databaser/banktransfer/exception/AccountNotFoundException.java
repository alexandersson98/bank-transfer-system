package se.chasacademy.databaser.banktransfer.exception;

public class AccountNotFoundException extends ValidationException {
    public AccountNotFoundException(Long id) {
        super("Account not found " + id );
    }

}
